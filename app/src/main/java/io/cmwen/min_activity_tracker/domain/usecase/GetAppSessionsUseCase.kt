package io.cmwen.min_activity_tracker.domain.usecase

import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import io.cmwen.min_activity_tracker.domain.repository.AppSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppSessionsUseCase @Inject constructor(
    private val repository: AppSessionRepository
) {
    operator fun invoke(): Flow<List<AppSessionEntity>> {
        return repository.getAllSessions()
    }
}
