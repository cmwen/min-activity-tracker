package io.cmwen.min_activity_tracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmwen.min_activity_tracker.data.preferences.UserPreferences
import io.cmwen.min_activity_tracker.data.preferences.UserPreferencesRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.cmwen.min_activity_tracker.features.export.DataExporter
import io.cmwen.min_activity_tracker.features.export.ExportFormat
import io.cmwen.min_activity_tracker.features.export.ExportTimeRange
import io.cmwen.min_activity_tracker.features.workers.WorkScheduler
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        private val dataExporter: DataExporter,
        private val sessionRepository: SessionRepository,
        private val workScheduler: WorkScheduler,
    ) : ViewModel() {
        private val exportState = MutableStateFlow<ExportState>(ExportState.Idle)

        val uiState: StateFlow<SettingsUiState> =
            combine(
                userPreferencesRepository.preferencesFlow,
                exportState,
            ) { preferences, export ->
                SettingsUiState(
                    preferences = preferences,
                    exportState = export,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState(),
            )

        fun onAppUsageTrackingChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setCollectAppUsage(enabled)
                updateWorkSchedules()
            }
        }

        fun onBatteryTrackingChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setCollectBattery(enabled)
                updateWorkSchedules()
            }
        }

        fun onLocationTrackingChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setCollectLocation(enabled)
            }
        }

        fun onAnonymizeLocationChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setAnonymizeLocation(enabled)
                if (enabled) {
                    sessionRepository.clearLocationData()
                }
            }
        }

        fun onActivityRecognitionChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setCollectActivityRecognition(enabled)
            }
        }

        fun onAutoExportEnabledChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setAutoExportEnabled(enabled)
                updateAutoExportSchedule()
            }
        }

        fun onAutoExportFormatChanged(format: ExportFormat) {
            viewModelScope.launch {
                userPreferencesRepository.setAutoExportFormat(format)
                updateAutoExportSchedule()
            }
        }

        fun onAutoExportRangeChanged(range: ExportTimeRange) {
            viewModelScope.launch {
                userPreferencesRepository.setAutoExportRange(range)
                updateAutoExportSchedule()
            }
        }

        fun onAutoExportAnonymizeChanged(enabled: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setAutoExportAnonymize(enabled)
            }
        }

        fun clearSavedLocationData() {
            viewModelScope.launch {
                sessionRepository.clearLocationData()
            }
        }

        fun resetExportState() {
            exportState.update { ExportState.Idle }
        }

        fun exportData(
            format: ExportFormat,
            anonymize: Boolean,
            timeRange: ExportTimeRange,
        ) {
            viewModelScope.launch {
                exportState.value = ExportState.InProgress
                val (start, end) =
                    when (timeRange) {
                        ExportTimeRange.ALL -> 0L to System.currentTimeMillis()
                        ExportTimeRange.LAST_24_HOURS -> {
                            val endTime = System.currentTimeMillis()
                            (endTime - ONE_DAY_MS) to endTime
                        }
                    }
                val result =
                    when (format) {
                        ExportFormat.JSON -> dataExporter.exportToJson(start, end, anonymize)
                        ExportFormat.CSV -> dataExporter.exportSessionsToCsv(start, end, anonymize)
                    }

                exportState.value =
                    result.fold(
                        onSuccess = { file -> ExportState.Success(file) },
                        onFailure = { throwable -> ExportState.Error(throwable) },
                    )
            }
        }

        private suspend fun updateWorkSchedules() {
            val prefs = userPreferencesRepository.preferencesFlow.first()
            if (prefs.hasAnyCollectionEnabled) {
                workScheduler.scheduleDataCollection()
            } else {
                workScheduler.cancelDataCollectionWork()
            }
        }

        private suspend fun updateAutoExportSchedule() {
            val prefs = userPreferencesRepository.preferencesFlow.first()
            if (prefs.autoExportEnabled) {
                workScheduler.scheduleAutoExport()
            } else {
                workScheduler.cancelAutoExportWork()
            }
        }

        companion object {
            private const val ONE_DAY_MS = 24 * 60 * 60 * 1000L
        }
    }

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val exportState: ExportState = ExportState.Idle,
)

sealed interface ExportState {
    data object Idle : ExportState
    data object InProgress : ExportState
    data class Success(val file: File) : ExportState
    data class Error(val throwable: Throwable) : ExportState
}
