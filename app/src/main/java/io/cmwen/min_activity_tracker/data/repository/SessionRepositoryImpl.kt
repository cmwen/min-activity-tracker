package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.SessionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SessionRepositoryImpl(
    private val dao: SessionDao,
) : SessionRepository {
    override fun observeSessions(): Flow<List<AppSessionEntity>> = dao.getAllSessions()

    override suspend fun getAllSessions(): List<AppSessionEntity> = dao.getAllSessions().first()

    override suspend fun getSessionsInRange(
        startTime: Long,
        endTime: Long,
    ): List<AppSessionEntity> = dao.getSessionsInRange(startTime, endTime)

    override suspend fun getSessionById(id: String): AppSessionEntity? = dao.getSessionById(id)

    override suspend fun insert(session: AppSessionEntity) = dao.insert(session)

    override suspend fun insertAll(sessions: List<AppSessionEntity>) = dao.insertAll(sessions)

    override suspend fun deleteById(id: String) = dao.deleteById(id)

    override suspend fun deleteSessionsOlderThan(timestamp: Long) = dao.deleteSessionsOlderThan(timestamp)
}
