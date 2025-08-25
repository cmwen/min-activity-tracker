package io.cmwen.min_activity_tracker.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var instance: MinActivityDatabase? = null

    fun getDatabase(context: Context): MinActivityDatabase =
        instance ?: synchronized(this) {
            val instance =
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        MinActivityDatabase::class.java,
                        "min_activity_database",
                    )
                    // Pass migrations explicitly to avoid an unnecessary spread copy
                    .addMigrations(DatabaseMigrations.MIGRATION_1_2)
                    .fallbackToDestructiveMigration() // For development phase
                    .build()
            this.instance = instance
            instance
        }
}
