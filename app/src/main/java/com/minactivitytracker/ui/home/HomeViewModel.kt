package com.minactivitytracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.data.entity.AppSession
import com.minactivitytracker.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: ActivityRepository
) : ViewModel() {

    val recentSessions: StateFlow<List<AppSessionUiModel>> = repository.getAllSessions()
        .map { sessions ->
            sessions.take(20).map { it.toUiModel() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun AppSession.toUiModel(): AppSessionUiModel {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return AppSessionUiModel(
            packageName = packageName,
            startTime = dateFormat.format(Date(startTimestamp)),
            duration = formatDuration(durationMs)
        )
    }

    private fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return if (hours > 0) {
            "${hours}h ${minutes % 60}m"
        } else {
            "${minutes}m ${seconds % 60}s"
        }
    }
}

data class AppSessionUiModel(
    val packageName: String,
    val startTime: String,
    val duration: String
)
