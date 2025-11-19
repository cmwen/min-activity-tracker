package com.minactivitytracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minactivitytracker.data.entity.DeviceEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceEventDao {
    @Insert
    suspend fun insert(event: DeviceEvent)

    @Query("SELECT * FROM device_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<DeviceEvent>>
}
