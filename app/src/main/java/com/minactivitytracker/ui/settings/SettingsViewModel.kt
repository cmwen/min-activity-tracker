package com.minactivitytracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.repository.AutoExportInterval
import com.minactivitytracker.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isUsageTrackingEnabled: StateFlow<Boolean> = settingsRepository.isUsageTrackingEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isBatteryTrackingEnabled: StateFlow<Boolean> = settingsRepository.isBatteryTrackingEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val autoExportInterval: StateFlow<AutoExportInterval> = settingsRepository.autoExportInterval
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AutoExportInterval.NONE)

    fun setUsageTrackingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setUsageTrackingEnabled(enabled)
        }
    }

    fun setBatteryTrackingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBatteryTrackingEnabled(enabled)
        }
    }

    fun setAutoExportInterval(interval: AutoExportInterval) {
        viewModelScope.launch {
            settingsRepository.setAutoExportInterval(interval)
            scheduleAutoExport(interval)
        }
    }

    private fun scheduleAutoExport(interval: AutoExportInterval) {
        val workManager = androidx.work.WorkManager.getInstance(settingsRepository.getContext())
        val workName = "auto_export"

        if (interval == AutoExportInterval.NONE) {
            workManager.cancelUniqueWork(workName)
        } else {
            val repeatInterval = when (interval) {
                AutoExportInterval.DAILY -> 1L
                AutoExportInterval.WEEKLY -> 7L
                else -> 1L // Should not happen
            }
            
            val request = androidx.work.PeriodicWorkRequestBuilder<com.minactivitytracker.service.AutoExportWorker>(
                repeatInterval, java.util.concurrent.TimeUnit.DAYS
            ).build()

            workManager.enqueueUniquePeriodicWork(
                workName,
                androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }
}
