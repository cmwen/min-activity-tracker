package io.cmwen.min_activity_tracker.data

import io.cmwen.min_activity_tracker.data.database.DatabaseMigrations
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for database-related logic that doesn't require Android instrumentation
 */
class MinActivityDatabaseTest {
    @Test
    fun migration_sql_constant_isCorrect() {
        assertEquals(
            "ALTER TABLE app_sessions ADD COLUMN notes TEXT",
            DatabaseMigrations.MIGRATION_1_2_SQL,
        )
    }

    @Test
    fun migration_version_numbers_areCorrect() {
        assertEquals(1, DatabaseMigrations.MIGRATION_1_2.startVersion)
        assertEquals(2, DatabaseMigrations.MIGRATION_1_2.endVersion)
    }
}
