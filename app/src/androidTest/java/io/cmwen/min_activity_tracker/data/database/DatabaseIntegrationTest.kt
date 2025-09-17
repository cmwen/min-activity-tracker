package io.cmwen.min_activity_tracker.data.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/**
 * Integration tests for Room database functionality
 * These tests require Android instrumentation and run on device/emulator
 */
@RunWith(AndroidJUnit4::class)
class DatabaseIntegrationTest {

    @Test
    fun writeAndReadSession() =
        runTest {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        context,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val sessionDao = database.sessionDao()
            val session =
                AppSessionEntity(
                    id = UUID.randomUUID().toString(),
                    packageName = "com.test.app",
                    appLabel = "Test App",
                    startTimestamp = System.currentTimeMillis(),
                    endTimestamp = System.currentTimeMillis() + 60000,
                    durationMs = 60000,
                )

            sessionDao.insert(session)
            val sessions = sessionDao.getAllSessions().first()

            assertEquals(1, sessions.size)
            assertEquals(session.packageName, sessions[0].packageName)
            assertEquals(session.appLabel, sessions[0].appLabel)

            database.close()
        }

    @Test
    fun writeAndReadDeviceEvent() =
        runTest {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        context,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val deviceEventDao = database.deviceEventDao()
            val event =
                DeviceEventEntity(
                    id = UUID.randomUUID().toString(),
                    type = "SCREEN_ON",
                    timestamp = System.currentTimeMillis(),
                )

            deviceEventDao.insert(event)
            val events = deviceEventDao.getAllEvents().first()

            assertEquals(1, events.size)
            assertEquals(event.type, events[0].type)

            database.close()
        }

    @Test
    fun writeAndReadBatterySample() =
        runTest {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        context,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val batterySampleDao = database.batterySampleDao()
            val sample =
                BatterySampleEntity(
                    id = UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis(),
                    levelPercent = 85,
                    chargingState = "DISCHARGING",
                )

            batterySampleDao.insert(sample)
            val samples = batterySampleDao.getAllSamples().first()

            assertEquals(1, samples.size)
            assertEquals(sample.levelPercent, samples[0].levelPercent)
            assertEquals(sample.chargingState, samples[0].chargingState)

            database.close()
        }

    @Test
    fun database_creation_and_allDaos_available() =
        runTest {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        context,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            // Test that all DAOs can be obtained without errors
            val sessionDao = database.sessionDao()
            val deviceEventDao = database.deviceEventDao()
            val batterySampleDao = database.batterySampleDao()
            val analysisReportDao = database.analysisReportDao()

            // Verify they're not null (basic sanity check)
            assert(sessionDao != null)
            assert(deviceEventDao != null)
            assert(batterySampleDao != null)
            assert(analysisReportDao != null)

            database.close()
        }
}
