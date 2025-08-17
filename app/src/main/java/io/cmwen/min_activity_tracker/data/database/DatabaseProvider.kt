package io.cmwen.min_activity_tracker.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: MinActivityDatabase? = null

    fun getDatabase(context: Context): MinActivityDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MinActivityDatabase::class.java,
                "min_activity_database"
            )
                .fallbackToDestructiveMigration() // For development phase
                .build()
            INSTANCE = instance
            instance
        }
    }
}