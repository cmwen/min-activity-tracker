package io.cmwen.min_activity_tracker.domain.repository

import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import kotlinx.coroutines.flow.Flow

interface AppSessionRepository {
    fun getAllSessions(): Flow<List<AppSessionEntity>>
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<AppSessionEntity>>
    suspend fun insertSession(session: AppSessionEntity)
    suspend fun clearAll()
}
