package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observeSessions(): Flow<List<AppSessionEntity>>

    suspend fun getAllSessions(): List<AppSessionEntity>

    suspend fun getSessionsInRange(
        startTime: Long,
        endTime: Long,
    ): List<AppSessionEntity>

    suspend fun getSessionById(id: String): AppSessionEntity?

    suspend fun insert(session: AppSessionEntity)

    suspend fun insertAll(sessions: List<AppSessionEntity>)

    suspend fun deleteById(id: String)

    suspend fun deleteSessionsOlderThan(timestamp: Long)
}
