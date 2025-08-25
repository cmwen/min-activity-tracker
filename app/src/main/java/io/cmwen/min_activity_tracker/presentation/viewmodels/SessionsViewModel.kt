package io.cmwen.min_activity_tracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel
    @Inject
    constructor(
        private val repo: SessionRepository,
    ) : ViewModel() {
        val sessions: StateFlow<List<AppSessionEntity>> =
            repo
                .observeSessions()
                .map { it }
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        suspend fun add(session: AppSessionEntity) = repo.insert(session)

        suspend fun remove(id: String) = repo.deleteById(id)
    }
