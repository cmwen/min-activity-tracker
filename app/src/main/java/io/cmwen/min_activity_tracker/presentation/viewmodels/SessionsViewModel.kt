package io.cmwen.min_activity_tracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SessionsViewModel(
    private val repo: SessionRepository
) : ViewModel() {

    val sessions: StateFlow<List<AppSessionEntity>> = repo.observeSessions()
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    suspend fun add(session: AppSessionEntity) = repo.insert(session)
    suspend fun remove(id: String) = repo.deleteById(id)
}

// Simple factory for manual ViewModel creation in absence of DI
class SessionsViewModelFactory(private val repo: SessionRepository) {
    fun create(): SessionsViewModel = SessionsViewModel(repo)
}
