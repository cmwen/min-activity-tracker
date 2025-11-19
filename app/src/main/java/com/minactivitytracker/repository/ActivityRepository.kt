package com.minactivitytracker.repository

import com.minactivitytracker.data.dao.AppSessionDao
import com.minactivitytracker.data.dao.DeviceEventDao
import com.minactivitytracker.data.entity.AppSession
import com.minactivitytracker.data.entity.DeviceEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepository @Inject constructor(
    private val appSessionDao: AppSessionDao,
    private val deviceEventDao: DeviceEventDao
) {
    fun getAllSessions(): Flow<List<AppSession>> = appSessionDao.getAllSessions()

    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<AppSession>> =
        appSessionDao.getSessionsInRange(startTime, endTime)

    suspend fun insertSession(session: AppSession) = appSessionDao.insert(session)

    fun getAllEvents(): Flow<List<DeviceEvent>> = deviceEventDao.getAllEvents()

    suspend fun insertEvent(event: DeviceEvent) = deviceEventDao.insert(event)
}
