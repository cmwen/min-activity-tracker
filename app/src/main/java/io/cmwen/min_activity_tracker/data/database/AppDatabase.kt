package io.cmwen.min_activity_tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.cmwen.min_activity_tracker.data.database.dao.AppSessionDao
import io.cmwen.min_activity_tracker.data.database.dao.BatteryDao
import io.cmwen.min_activity_tracker.data.database.dao.DeviceEventDao
import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.entities.BatterySampleEntity
import io.cmwen.min_activity_tracker.data.database.entities.DeviceEventEntity

@Database(
    entities = [
        AppSessionEntity::class,
        BatterySampleEntity::class,
        DeviceEventEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appSessionDao(): AppSessionDao
    abstract fun batteryDao(): BatteryDao
    abstract fun deviceEventDao(): DeviceEventDao
}
