package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observeSessions(): Flow<List<AppSessionEntity>>

    suspend fun insert(session: AppSessionEntity)

    suspend fun deleteById(id: String)
}
