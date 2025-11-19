package io.cmwen.min_activity_tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.cmwen.min_activity_tracker.data.database.entities.DeviceEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceEventDao {
    @Query("SELECT * FROM device_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<DeviceEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: DeviceEventEntity)
}
