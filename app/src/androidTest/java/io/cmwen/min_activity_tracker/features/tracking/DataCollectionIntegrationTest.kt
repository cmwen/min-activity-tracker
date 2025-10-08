package io.cmwen.min_activity_tracker.features.tracking

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.cmwen.min_activity_tracker.data.database.MinActivityDatabase
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.SessionRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DataCollectionIntegrationTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var database: MinActivityDatabase
    private lateinit var dataCollector: DataCollector

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    MinActivityDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        val sessionRepository = SessionRepositoryImpl(database.sessionDao())
        val deviceEventRepository = DeviceEventRepositoryImpl(database.deviceEventDao())
        val batterySampleRepository = BatterySampleRepositoryImpl(database.batterySampleDao())

        dataCollector =
            DataCollector(
                context,
                sessionRepository,
                deviceEventRepository,
                batterySampleRepository,
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testBatteryDataCollection() =
        runTest {
            // When
            dataCollector.collectBatteryData()

            // Then
            val samples = database.batterySampleDao().getAllSamples().first()
            assertThat(samples).isNotEmpty()
            assertThat(samples[0].levelPercent).isAtLeast(-1)
            assertThat(samples[0].chargingState).isNotNull()
        }

    @Test
    fun testCollectDataSnapshot() =
        runTest {
            // When
            dataCollector.collectDataSnapshot()

            // Then - should have collected battery data at minimum
            val samples = database.batterySampleDao().getAllSamples().first()
            assertThat(samples).isNotEmpty()
        }

    @Test
    fun testMultipleBatterySamples() =
        runTest {
            // When - collect multiple samples
            dataCollector.collectBatteryData()
            Thread.sleep(100) // Ensure different timestamps
            dataCollector.collectBatteryData()

            // Then
            val samples = database.batterySampleDao().getAllSamples().first()
            assertThat(samples.size).isAtLeast(2)
            // Verify samples have different timestamps
            if (samples.size >= 2) {
                assertThat(samples[0].timestamp).isNotEqualTo(samples[1].timestamp)
            }
        }

    @Test
    fun testUsageDataCollectionWithEmptyRange() =
        runTest {
            // Given - a very recent time range with likely no app usage
            val now = System.currentTimeMillis()
            val oneSecondAgo = now - 1000L

            // When
            dataCollector.collectUsageData(oneSecondAgo, now)

            // Then - should complete without errors
            val sessions = database.sessionDao().getAllSessions().first()
            // May be empty or have some sessions depending on actual usage
            assertThat(sessions).isNotNull()
        }

    @Test
    fun testBatteryDataHasValidValues() =
        runTest {
            // When
            dataCollector.collectBatteryData()

            // Then
            val samples = database.batterySampleDao().getAllSamples().first()
            assertThat(samples).isNotEmpty()

            val sample = samples[0]
            // Battery level should be between -1 (unknown) and 100
            assertThat(sample.levelPercent).isAtLeast(-1)
            assertThat(sample.levelPercent).isAtMost(100)

            // Charging state should be one of the valid states
            assertThat(sample.chargingState).isIn(
                listOf(
                    "CHARGING",
                    "DISCHARGING",
                    "FULL",
                    "NOT_CHARGING",
                    "UNKNOWN",
                ),
            )

            // Timestamp should be recent (within last 10 seconds)
            val timeDiff = System.currentTimeMillis() - sample.timestamp
            assertThat(timeDiff).isLessThan(10000L)
        }
}
