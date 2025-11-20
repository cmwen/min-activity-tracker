package com.minactivitytracker.ui.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.data.AppCategory
import com.minactivitytracker.data.AppCategoryClassifier
import com.minactivitytracker.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

enum class TimePeriod {
    TODAY, WEEK, MONTH, ALL_TIME;

    fun displayName(): String = when (this) {
        TODAY -> "Today"
        WEEK -> "This Week"
        MONTH -> "This Month"
        ALL_TIME -> "All Time"
    }
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    repository: ActivityRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(TimePeriod.WEEK)
    val selectedPeriod: StateFlow<TimePeriod> = _selectedPeriod.asStateFlow()

    val appListState: StateFlow<AppListState> = combine(
        repository.getAllSessions(),
        _selectedPeriod
    ) { sessions, period ->
        val filteredSessions = filterSessionsByPeriod(sessions, period)
        
        val categoryUsage = filteredSessions
            .groupBy { AppCategoryClassifier.classify(it.packageName) }
            .map { (category, sessions) ->
                CategoryUsage(
                    category = category,
                    totalTime = sessions.sumOf { it.durationMs },
                    appCount = sessions.distinctBy { it.packageName }.size,
                    sessionCount = sessions.size
                )
            }
            .sortedByDescending { it.totalTime }

        val appUsageList = filteredSessions
            .groupBy { it.packageName }
            .map { (packageName, sessions) ->
                AppUsageUiModel(
                    packageName = packageName,
                    category = AppCategoryClassifier.classify(packageName),
                    totalTime = sessions.sumOf { it.durationMs },
                    sessionCount = sessions.size,
                    lastUsed = sessions.maxOfOrNull { it.startTimestamp } ?: 0L
                )
            }
            .sortedByDescending { it.totalTime }

        AppListState(
            categoryUsage = categoryUsage,
            appUsageList = appUsageList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppListState()
    )

    fun selectPeriod(period: TimePeriod) {
        _selectedPeriod.value = period
    }

    private fun filterSessionsByPeriod(sessions: List<com.minactivitytracker.data.entity.AppSession>, period: TimePeriod): List<com.minactivitytracker.data.entity.AppSession> {
        val now = System.currentTimeMillis()
        val startTime = when (period) {
            TimePeriod.TODAY -> {
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }
            TimePeriod.WEEK -> {
                Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }
            TimePeriod.MONTH -> {
                Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }
            TimePeriod.ALL_TIME -> 0L
        }
        
        return sessions.filter { it.startTimestamp >= startTime }
    }

    fun formatDuration(millis: Long): String {
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

data class AppListState(
    val categoryUsage: List<CategoryUsage> = emptyList(),
    val appUsageList: List<AppUsageUiModel> = emptyList()
)

data class CategoryUsage(
    val category: AppCategory,
    val totalTime: Long,
    val appCount: Int,
    val sessionCount: Int
)

data class AppUsageUiModel(
    val packageName: String,
    val category: AppCategory,
    val totalTime: Long,
    val sessionCount: Int,
    val lastUsed: Long
)
