package io.cmwen.min_activity_tracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceEventDao {
    @Query("SELECT * FROM device_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<DeviceEventEntity>>

    @Query("SELECT * FROM device_events WHERE type = :type ORDER BY timestamp DESC")
    fun getEventsByType(type: String): Flow<List<DeviceEventEntity>>

    @Query("SELECT * FROM device_events WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getEventsByTimeRange(
        startTime: Long,
        endTime: Long,
    ): Flow<List<DeviceEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: DeviceEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<DeviceEventEntity>)

    @Query("DELETE FROM device_events WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM device_events WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOldEvents(beforeTimestamp: Long)
}
