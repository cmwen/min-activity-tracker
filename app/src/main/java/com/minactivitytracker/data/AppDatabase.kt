package com.minactivitytracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minactivitytracker.data.dao.AppSessionDao
import com.minactivitytracker.data.dao.BatterySampleDao
import com.minactivitytracker.data.dao.DeviceEventDao
import com.minactivitytracker.data.entity.AppSession
import com.minactivitytracker.data.entity.BatterySample
import com.minactivitytracker.data.entity.DeviceEvent

@Database(
    entities = [AppSession::class, DeviceEvent::class, BatterySample::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appSessionDao(): AppSessionDao
    abstract fun deviceEventDao(): DeviceEventDao
    abstract fun batterySampleDao(): BatterySampleDao
}
