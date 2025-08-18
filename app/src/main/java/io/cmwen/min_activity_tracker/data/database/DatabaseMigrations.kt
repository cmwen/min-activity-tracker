package io.cmwen.min_activity_tracker.data.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    private const val TAG = "DatabaseMigrations"

    // Exported migration list for Room registration.
    val ALL: Array<Migration>
        get() = arrayOf(MIGRATION_1_2)

    // Migration from version 1 -> 2: Add a nullable column `notes` to `app_sessions`.
    // This is a safe additive migration that won't affect existing rows.
    const val MIGRATION_1_2_SQL = "ALTER TABLE app_sessions ADD COLUMN notes TEXT"
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add a nullable TEXT column `notes` to app_sessions.
            // Room will map String? to TEXT.
            database.execSQL(MIGRATION_1_2_SQL)
            Log.i(TAG, "Applied MIGRATION_1_2: added 'notes' column to app_sessions")
        }
    }
}
