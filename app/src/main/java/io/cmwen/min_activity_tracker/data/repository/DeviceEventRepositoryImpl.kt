package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.DeviceEventDao
import io.cmwen.min_activity_tracker.data.database.DeviceEventEntity
import kotlinx.coroutines.flow.Flow

class DeviceEventRepositoryImpl(
    private val dao: DeviceEventDao
) : DeviceEventRepository {
    override fun observeAllEvents(): Flow<List<DeviceEventEntity>> = dao.getAllEvents()
    
    override fun observeEventsByType(type: String): Flow<List<DeviceEventEntity>> = 
        dao.getEventsByType(type)
    
    override fun observeEventsByTimeRange(startTime: Long, endTime: Long): Flow<List<DeviceEventEntity>> = 
        dao.getEventsByTimeRange(startTime, endTime)
    
    override suspend fun insert(event: DeviceEventEntity) = dao.insert(event)
    
    override suspend fun insertAll(events: List<DeviceEventEntity>) = dao.insertAll(events)
    
    override suspend fun deleteById(id: String) = dao.deleteById(id)
    
    override suspend fun deleteOldEvents(beforeTimestamp: Long) = dao.deleteOldEvents(beforeTimestamp)
}
