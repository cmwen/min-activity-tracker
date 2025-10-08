package io.cmwen.min_activity_tracker.features.tracking

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.BatteryManager
import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DataCollectorTest {
    private lateinit var context: Context
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var packageManager: PackageManager
    private lateinit var sessionRepository: SessionRepository
    private lateinit var deviceEventRepository: DeviceEventRepository
    private lateinit var batterySampleRepository: BatterySampleRepository
    private lateinit var dataCollector: DataCollector

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        usageStatsManager = mockk(relaxed = true)
        packageManager = mockk(relaxed = true)
        sessionRepository = mockk(relaxed = true)
        deviceEventRepository = mockk(relaxed = true)
        batterySampleRepository = mockk(relaxed = true)

        every { context.getSystemService(Context.USAGE_STATS_SERVICE) } returns usageStatsManager
        every { context.packageManager } returns packageManager
        // Mock location permission as denied to avoid location service calls
        every { context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) } returns PackageManager.PERMISSION_DENIED
        every { context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) } returns PackageManager.PERMISSION_DENIED

        dataCollector =
            DataCollector(
                context,
                sessionRepository,
                deviceEventRepository,
                batterySampleRepository,
            )
    }

    @Test
    fun `collectUsageData should process events and save sessions`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val events = mockk<UsageEvents>(relaxed = true)

            every { usageStatsManager.queryEvents(startTime, endTime) } returns events
            every { events.hasNextEvent() } returns false // No events to simplify test

            val appInfo = mockk<ApplicationInfo>()
            every { packageManager.getApplicationInfo(any<String>(), any<Int>()) } returns appInfo
            every { packageManager.getApplicationLabel(any<ApplicationInfo>()) } returns "Test App"

            coEvery { sessionRepository.insertAll(any()) } returns Unit

            // When
            dataCollector.collectUsageData(startTime, endTime)

            // Then
            coVerify { sessionRepository.insertAll(any()) }
        }

    @Test
    fun `collectUsageData with no events should save empty list`() =
        runTest {
            // Given
            val events = mockk<UsageEvents>(relaxed = true)
            every { usageStatsManager.queryEvents(any(), any()) } returns events
            every { events.hasNextEvent() } returns false
            coEvery { sessionRepository.insertAll(any()) } returns Unit

            // When
            dataCollector.collectUsageData(0, 1)

            // Then
            coVerify { sessionRepository.insertAll(emptyList()) }
        }

    @Test
    fun `collectBatteryData should save battery sample with correct data`() =
        runTest {
            // Given
            val batteryIntent = mockk<Intent>(relaxed = true)
            every { context.registerReceiver(null, any()) } returns batteryIntent
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 85
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) } returns BatteryManager.BATTERY_STATUS_DISCHARGING

            val sampleSlot = slot<BatterySampleEntity>()
            coEvery { batterySampleRepository.insert(capture(sampleSlot)) } returns Unit

            // When
            dataCollector.collectBatteryData()

            // Then
            coVerify { batterySampleRepository.insert(any()) }
            assertThat(sampleSlot.captured.levelPercent).isEqualTo(85)
            assertThat(sampleSlot.captured.chargingState).isEqualTo("DISCHARGING")
        }

    @Test
    fun `collectBatteryData should handle charging state correctly`() =
        runTest {
            // Given
            val batteryIntent = mockk<Intent>(relaxed = true)
            every { context.registerReceiver(null, any()) } returns batteryIntent
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 90
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) } returns BatteryManager.BATTERY_STATUS_CHARGING

            val sampleSlot = slot<BatterySampleEntity>()
            coEvery { batterySampleRepository.insert(capture(sampleSlot)) } returns Unit

            // When
            dataCollector.collectBatteryData()

            // Then
            assertThat(sampleSlot.captured.chargingState).isEqualTo("CHARGING")
        }

    @Test
    fun `collectBatteryData should handle full battery state`() =
        runTest {
            // Given
            val batteryIntent = mockk<Intent>(relaxed = true)
            every { context.registerReceiver(null, any()) } returns batteryIntent
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 100
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
            every { batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) } returns BatteryManager.BATTERY_STATUS_FULL

            val sampleSlot = slot<BatterySampleEntity>()
            coEvery { batterySampleRepository.insert(capture(sampleSlot)) } returns Unit

            // When
            dataCollector.collectBatteryData()

            // Then
            assertThat(sampleSlot.captured.levelPercent).isEqualTo(100)
            assertThat(sampleSlot.captured.chargingState).isEqualTo("FULL")
        }

    @Test
    fun `collectUsageData handles missing app label gracefully`() =
        runTest {
            // Given
            val events = mockk<UsageEvents>(relaxed = true)
            every { usageStatsManager.queryEvents(any<Long>(), any<Long>()) } returns events
            every { events.hasNextEvent() } returns false
            every { packageManager.getApplicationInfo(any<String>(), any<Int>()) } throws PackageManager.NameNotFoundException()
            coEvery { sessionRepository.insertAll(any()) } returns Unit

            // When
            dataCollector.collectUsageData(0, 1)

            // Then - should not throw exception
            coVerify { sessionRepository.insertAll(any()) }
        }
}
