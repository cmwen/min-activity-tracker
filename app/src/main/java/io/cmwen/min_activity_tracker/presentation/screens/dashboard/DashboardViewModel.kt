package io.cmwen.min_activity_tracker.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import io.cmwen.min_activity_tracker.domain.usecase.GetAppSessionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getAppSessionsUseCase: GetAppSessionsUseCase
) : ViewModel() {

    val sessions: StateFlow<List<AppSessionEntity>> = getAppSessionsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
