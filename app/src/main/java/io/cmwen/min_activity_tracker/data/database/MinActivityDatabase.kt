package io.cmwen.min_activity_tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AppSessionEntity::class,
        DeviceEventEntity::class,
        BatterySampleEntity::class,
        AnalysisReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MinActivityDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    // Additional DAOs can be added here
}
