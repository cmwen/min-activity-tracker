package io.cmwen.min_activity_tracker.data.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Integration tests for Room database functionality
 * These tests require Android instrumentation and run on device/emulator
 */
@RunWith(AndroidJUnit4::class)
class DatabaseIntegrationTest {
    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MinActivityDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory(),
        )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db =
            helper.createDatabase(TEST_DB, 1).apply {
                // Database has schema version 1. Insert some data using SQL queries.
                // You can't use DAO classes because they expect the latest schema.
                execSQL("INSERT INTO app_sessions (id, package_name, start_time, end_time) VALUES (1, 'com.test', 1000, 2000)")

                // Prepare for the next version.
                close()
            }

        // Re-open the database with version 2 and provide MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, DatabaseMigrations.MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you can also validate that the data was preserved.
        db.query("SELECT * FROM app_sessions").use { cursor ->
            assertEquals(1, cursor.count)
            cursor.moveToFirst()
            assertEquals(1, cursor.getLong(cursor.getColumnIndexOrThrow("id")))
            assertEquals("com.test", cursor.getString(cursor.getColumnIndexOrThrow("package_name")))
            // The notes column should be null for migrated records
            val notesIndex = cursor.getColumnIndexOrThrow("notes")
            assertEquals(true, cursor.isNull(notesIndex))
        }
    }

    @Test
    fun writeAndReadSession() =
        runTest {
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val sessionDao = database.sessionDao()
            val session =
                AppSessionEntity(
                    id = 1,
                    packageName = "com.test.app",
                    startTime = System.currentTimeMillis(),
                    endTime = System.currentTimeMillis() + 60000,
                    notes = "Test session",
                )

            sessionDao.insertSession(session)
            val sessions = sessionDao.getAllSessions()

            assertEquals(1, sessions.size)
            assertEquals(session.packageName, sessions[0].packageName)
            assertEquals(session.notes, sessions[0].notes)

            database.close()
        }

    @Test
    fun writeAndReadDeviceEvent() =
        runTest {
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val deviceEventDao = database.deviceEventDao()
            val event =
                DeviceEventEntity(
                    id = 1,
                    eventType = "SCREEN_ON",
                    timestamp = System.currentTimeMillis(),
                )

            deviceEventDao.insertEvent(event)
            val events = deviceEventDao.getAllEvents()

            assertEquals(1, events.size)
            assertEquals(event.eventType, events[0].eventType)

            database.close()
        }

    @Test
    fun writeAndReadBatterySample() =
        runTest {
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        MinActivityDatabase::class.java,
                    ).allowMainThreadQueries()
                    .build()

            val batterySampleDao = database.batterySampleDao()
            val sample =
                BatterySampleEntity(
                    id = 1,
                    timestamp = System.currentTimeMillis(),
                    batteryLevel = 85,
                    isCharging = false,
                )

            batterySampleDao.insertSample(sample)
            val samples = batterySampleDao.getAllSamples()

            assertEquals(1, samples.size)
            assertEquals(sample.batteryLevel, samples[0].batteryLevel)
            assertEquals(sample.isCharging, samples[0].isCharging)

            database.close()
        }

    @Test
    fun database_creation_and_allDaos_available() =
        runTest {
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().targetContext,
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

    companion object {
        private const val TEST_DB = "migration-test"
    }
}
