package io.cmwen.min_activity_tracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SummariesViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val summaries: StateFlow<Map<String, Long>> =
        sessionRepository.observeSessions()
            .map { sessions ->
                sessions.groupBy { it.packageName }
                    .mapValues { (_, sessions) -> sessions.sumOf { it.durationMs } }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
}
