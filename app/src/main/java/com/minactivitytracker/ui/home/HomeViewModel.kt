package com.minactivitytracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.data.AppCategory
import com.minactivitytracker.data.AppCategoryClassifier
import com.minactivitytracker.data.entity.AppSession
import com.minactivitytracker.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: ActivityRepository
) : ViewModel() {

    val homeState: StateFlow<HomeUiState> = repository.getAllSessions()
        .map { sessions ->
            calculateHomeState(sessions)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    private fun calculateHomeState(sessions: List<AppSession>): HomeUiState {
        val now = System.currentTimeMillis()
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val weekStart = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val todaySessions = sessions.filter { it.startTimestamp >= todayStart }
        val weekSessions = sessions.filter { it.startTimestamp >= weekStart }

        val todayTotalMs = todaySessions.sumOf { it.durationMs }
        val weekTotalMs = weekSessions.sumOf { it.durationMs }

        val topAppsToday = todaySessions
            .groupBy { it.packageName }
            .map { (pkg, sessions) ->
                TopAppItem(
                    packageName = pkg,
                    totalTime = sessions.sumOf { it.durationMs },
                    sessionCount = sessions.size
                )
            }
            .sortedByDescending { it.totalTime }
            .take(5)

        val categoryData = todaySessions
            .groupBy { AppCategoryClassifier.classify(it.packageName) }
            .map { (category, sessions) ->
                CategoryUsageItem(
                    category = category,
                    totalTime = sessions.sumOf { it.durationMs }
                )
            }
            .sortedByDescending { it.totalTime }

        val recentSessions = sessions.take(10).map { it.toUiModel() }

        return HomeUiState(
            todayTotalTime = formatDuration(todayTotalMs),
            weekTotalTime = formatDuration(weekTotalMs),
            topAppsToday = topAppsToday,
            categoryData = categoryData,
            recentSessions = recentSessions
        )
    }

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
        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }
}

data class HomeUiState(
    val todayTotalTime: String = "0m",
    val weekTotalTime: String = "0m",
    val topAppsToday: List<TopAppItem> = emptyList(),
    val categoryData: List<CategoryUsageItem> = emptyList(),
    val recentSessions: List<AppSessionUiModel> = emptyList()
)

data class TopAppItem(
    val packageName: String,
    val totalTime: Long,
    val sessionCount: Int
)

data class CategoryUsageItem(
    val category: AppCategory,
    val totalTime: Long
)

data class AppSessionUiModel(
    val packageName: String,
    val startTime: String,
    val duration: String
)
