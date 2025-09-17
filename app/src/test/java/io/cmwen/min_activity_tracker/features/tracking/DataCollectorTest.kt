package io.cmwen.min_activity_tracker.features.tracking

import android.app.usage.UsageStatsManager
import android.content.Context
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DataCollectorTest {

    private lateinit var context: Context
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var sessionRepository: SessionRepository
    private lateinit var deviceEventRepository: DeviceEventRepository
    private lateinit var batterySampleRepository: BatterySampleRepository
    private lateinit var dataCollector: DataCollector

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        usageStatsManager = mockk(relaxed = true)
        sessionRepository = mockk(relaxed = true)
        deviceEventRepository = mockk(relaxed = true)
        batterySampleRepository = mockk(relaxed = true)
        every { context.getSystemService(Context.USAGE_STATS_SERVICE) } returns usageStatsManager
        dataCollector = DataCollector(
            context,
            sessionRepository,
            deviceEventRepository,
            batterySampleRepository
        )
    }

    @Test
    fun `collectUsageData should process events and save sessions`() = runTest {
        // Given
        val events = mockk<android.app.usage.UsageEvents>(relaxed = true)
        every { usageStatsManager.queryEvents(any(), any()) } returns events

        // When
        dataCollector.collectUsageData(0, 1)

        // Then
        io.mockk.coVerify { sessionRepository.insertAll(any()) }
    }

    @Test
    fun `collectBatteryData should save battery sample`() = runTest {
        // When
        dataCollector.collectBatteryData()

        // Then
        io.mockk.coVerify { batterySampleRepository.insert(any()) }
    }
}
