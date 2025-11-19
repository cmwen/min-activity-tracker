package com.minactivitytracker.ui.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    repository: ActivityRepository
) : ViewModel() {

    val appUsageList: StateFlow<List<AppUsageUiModel>> = repository.getAllSessions()
        .map { sessions ->
            sessions.groupBy { it.packageName }
                .map { (packageName, sessions) ->
                    AppUsageUiModel(
                        packageName = packageName,
                        sessionCount = sessions.size,
                        lastUsed = sessions.maxOfOrNull { it.startTimestamp } ?: 0L
                    )
                }
                .sortedByDescending { it.lastUsed }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

data class AppUsageUiModel(
    val packageName: String,
    val sessionCount: Int,
    val lastUsed: Long
)
