package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.dao.AppSessionDao
import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import io.cmwen.min_activity_tracker.domain.repository.AppSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSessionRepositoryImpl @Inject constructor(
    private val dao: AppSessionDao
) : AppSessionRepository {
    override fun getAllSessions(): Flow<List<AppSessionEntity>> = dao.getAllSessions()

    override fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<AppSessionEntity>> =
        dao.getSessionsInRange(startTime, endTime)

    override suspend fun insertSession(session: AppSessionEntity) = dao.insertSession(session)

    override suspend fun clearAll() = dao.clearAll()
}
