package io.cmwen.min_activity_tracker.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Integration tests for Room database
 * Tests database operations, migrations, and data integrity
 */
@RunWith(AndroidJUnit4::class)
class MinActivityDatabaseTest {
    private lateinit var database: MinActivityDatabase
    private lateinit var sessionDao: SessionDao
    private lateinit var batterySampleDao: BatterySampleDao
    private lateinit var deviceEventDao: DeviceEventDao
    private lateinit var analysisReportDao: AnalysisReportDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    MinActivityDatabase::class.java,
                ).build()

        sessionDao = database.sessionDao()
        batterySampleDao = database.batterySampleDao()
        deviceEventDao = database.deviceEventDao()
        analysisReportDao = database.analysisReportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndRetrieveAppSession() =
        runTest {
            // Given
            val session =
                AppSessionEntity(
                    id = "test_session_1",
                    packageName = "com.example.app",
                    appLabel = "Example App",
                    startTimestamp = 1000L,
                    endTimestamp = 2000L,
                    durationMs = 1000L,
                    startBatteryPct = 100,
                    endBatteryPct = 95,
                    locationLatitude = 37.7749,
                    locationLongitude = -122.4194,
                    metadataJson = null,
                )

            // When
            sessionDao.insertSession(session)
            val retrieved = sessionDao.getSessionById(session.id)

            // Then
            assertThat(retrieved).isNotNull()
            assertThat(retrieved?.id).isEqualTo(session.id)
            assertThat(retrieved?.packageName).isEqualTo(session.packageName)
            assertThat(retrieved?.appLabel).isEqualTo(session.appLabel)
            assertThat(retrieved?.durationMs).isEqualTo(session.durationMs)
            assertThat(retrieved?.locationLatitude).isEqualTo(session.locationLatitude)
        }

    @Test
    fun getSessionsInRange_returnsCorrectSessions() =
        runTest {
            // Given
            val session1 = createTestSession("session1", 1000L, 2000L)
            val session2 = createTestSession("session2", 3000L, 4000L)
            val session3 = createTestSession("session3", 5000L, 6000L)

            sessionDao.insertSession(session1)
            sessionDao.insertSession(session2)
            sessionDao.insertSession(session3)

            // When - Query range that includes session2 only
            val sessions = sessionDao.getSessionsInRange(2500L, 4500L)

            // Then
            assertThat(sessions).hasSize(1)
            assertThat(sessions[0].id).isEqualTo("session2")
        }

    @Test
    fun getAllSessionsFlow_emitsUpdates() =
        runTest {
            // Given
            val session1 = createTestSession("session1", 1000L, 2000L)

            // When
            sessionDao.insertSession(session1)
            val sessions = sessionDao.getAllSessionsFlow().first()

            // Then
            assertThat(sessions).hasSize(1)
            assertThat(sessions[0].id).isEqualTo("session1")
        }

    @Test
    fun deleteOldSessions_removesOnlyOldSessions() =
        runTest {
            // Given
            val oldSession = createTestSession("old", 1000L, 2000L)
            val recentSession = createTestSession("recent", 10000L, 11000L)

            sessionDao.insertSession(oldSession)
            sessionDao.insertSession(recentSession)

            // When - Delete sessions older than timestamp 5000
            sessionDao.deleteSessionsOlderThan(5000L)
            val remainingSessions = sessionDao.getAllSessionsFlow().first()

            // Then
            assertThat(remainingSessions).hasSize(1)
            assertThat(remainingSessions[0].id).isEqualTo("recent")
        }

    @Test
    fun insertBatterySample_andRetrieve() =
        runTest {
            // Given
            val sample =
                BatterySampleEntity(
                    id = "battery_1",
                    timestamp = 1000L,
                    levelPercent = 85,
                    chargingState = "DISCHARGING",
                    temperature = 30.5f,
                )

            // When
            batterySampleDao.insertSample(sample)
            val retrieved = batterySampleDao.getSamplesInRange(0L, 2000L)

            // Then
            assertThat(retrieved).hasSize(1)
            assertThat(retrieved[0].id).isEqualTo(sample.id)
            assertThat(retrieved[0].levelPercent).isEqualTo(sample.levelPercent)
            assertThat(retrieved[0].temperature).isEqualTo(sample.temperature)
        }

    @Test
    fun getAverageBatteryLevel_calculatesCorrectly() =
        runTest {
            // Given
            val sample1 = BatterySampleEntity("1", 1000L, 100, "FULL", 30f)
            val sample2 = BatterySampleEntity("2", 2000L, 80, "DISCHARGING", 32f)
            val sample3 = BatterySampleEntity("3", 3000L, 60, "DISCHARGING", 34f)

            batterySampleDao.insertSample(sample1)
            batterySampleDao.insertSample(sample2)
            batterySampleDao.insertSample(sample3)

            // When
            val average = batterySampleDao.getAverageBatteryLevel(0L, 4000L)

            // Then
            assertThat(average).isEqualTo(80.0) // (100 + 80 + 60) / 3 = 80
        }

    @Test
    fun insertDeviceEvent_andRetrieve() =
        runTest {
            // Given
            val event =
                DeviceEventEntity(
                    id = "event_1",
                    type = "SCREEN_ON",
                    timestamp = 1000L,
                    detailsJson = """{"brightness": 50}""",
                )

            // When
            deviceEventDao.insertEvent(event)
            val retrieved = deviceEventDao.getEventsInRange(0L, 2000L)

            // Then
            assertThat(retrieved).hasSize(1)
            assertThat(retrieved[0].id).isEqualTo(event.id)
            assertThat(retrieved[0].type).isEqualTo(event.type)
        }

    @Test
    fun getEventsByType_filtersCorrectly() =
        runTest {
            // Given
            val screenOnEvent = DeviceEventEntity("1", "SCREEN_ON", 1000L, null)
            val screenOffEvent = DeviceEventEntity("2", "SCREEN_OFF", 2000L, null)
            val chargingEvent = DeviceEventEntity("3", "CHARGING", 3000L, null)

            deviceEventDao.insertEvent(screenOnEvent)
            deviceEventDao.insertEvent(screenOffEvent)
            deviceEventDao.insertEvent(chargingEvent)

            // When
            val screenEvents = deviceEventDao.getEventsByType("SCREEN_ON")

            // Then
            assertThat(screenEvents).hasSize(1)
            assertThat(screenEvents[0].type).isEqualTo("SCREEN_ON")
        }

    @Test
    fun insertAnalysisReport_andRetrieve() =
        runTest {
            // Given
            val report =
                AnalysisReportEntity(
                    id = "report_1",
                    rangeStartTs = 1000L,
                    rangeEndTs = 2000L,
                    createdTs = 3000L,
                    reportType = "daily",
                    metricsJson = """{"totalDuration": 5000}""",
                )

            // When
            analysisReportDao.insertReport(report)
            val retrieved = analysisReportDao.getReportById(report.id)

            // Then
            assertThat(retrieved).isNotNull()
            assertThat(retrieved?.id).isEqualTo(report.id)
            assertThat(retrieved?.reportType).isEqualTo(report.reportType)
        }

    @Test
    fun getReportsByType_filtersCorrectly() =
        runTest {
            // Given
            val dailyReport1 = AnalysisReportEntity("1", 1000L, 2000L, 3000L, "daily", "{}")
            val dailyReport2 = AnalysisReportEntity("2", 4000L, 5000L, 6000L, "daily", "{}")
            val weeklyReport = AnalysisReportEntity("3", 1000L, 8000L, 9000L, "weekly", "{}")

            analysisReportDao.insertReport(dailyReport1)
            analysisReportDao.insertReport(dailyReport2)
            analysisReportDao.insertReport(weeklyReport)

            // When
            val dailyReports = analysisReportDao.getReportsByType("daily")

            // Then
            assertThat(dailyReports).hasSize(2)
            assertThat(dailyReports.all { it.reportType == "daily" }).isTrue()
        }

    @Test
    fun transactionRollback_onError() =
        runTest {
            // Given
            val session = createTestSession("session1", 1000L, 2000L)

            try {
                database.runInTransaction {
                    sessionDao.insertSession(session)
                    throw RuntimeException("Simulated error")
                }
            } catch (e: RuntimeException) {
                // Expected
            }

            // Then - Session should not be in database due to rollback
            val sessions = sessionDao.getAllSessionsFlow().first()
            assertThat(sessions).isEmpty()
        }

    private fun createTestSession(
        id: String,
        startTs: Long,
        endTs: Long,
    ): AppSessionEntity =
        AppSessionEntity(
            id = id,
            packageName = "com.test.app",
            appLabel = "Test App",
            startTimestamp = startTs,
            endTimestamp = endTs,
            durationMs = endTs - startTs,
            startBatteryPct = 100,
            endBatteryPct = 95,
            locationLatitude = null,
            locationLongitude = null,
            metadataJson = null,
        )
}
