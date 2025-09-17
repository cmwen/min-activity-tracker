package io.cmwen.min_activity_tracker.features.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionUIState())
    val uiState: StateFlow<PermissionUIState> = _uiState.asStateFlow()

    init {
        checkPermissions()
    }

    fun checkPermissions() {
        viewModelScope.launch {
            _uiState.value = PermissionUIState(
                hasUsageStats = permissionManager.isUsageStatsPermissionGranted(),
                hasAllPermissions = permissionManager.hasAllPermissions()
            )
        }
    }

    fun createUsageStatsIntent() = permissionManager.createUsageStatsIntent()

    fun createAppSettingsIntent() = permissionManager.createAppSettingsIntent()
}

data class PermissionUIState(
    val hasUsageStats: Boolean = false,
    val hasAllPermissions: Boolean = false
)
