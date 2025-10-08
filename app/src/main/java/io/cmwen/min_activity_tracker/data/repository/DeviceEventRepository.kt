package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.DeviceEventEntity
import kotlinx.coroutines.flow.Flow

interface DeviceEventRepository {
    fun observeAllEvents(): Flow<List<DeviceEventEntity>>

    fun observeEventsByType(type: String): Flow<List<DeviceEventEntity>>

    fun observeEventsByTimeRange(
        startTime: Long,
        endTime: Long,
    ): Flow<List<DeviceEventEntity>>

    suspend fun getEventsInRange(
        startTime: Long,
        endTime: Long,
    ): List<DeviceEventEntity>

    suspend fun insert(event: DeviceEventEntity)

    suspend fun insertAll(events: List<DeviceEventEntity>)

    suspend fun deleteById(id: String)

    suspend fun deleteOldEvents(beforeTimestamp: Long)
}
