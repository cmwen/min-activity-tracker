package io.cmwen.min_activity_tracker.features.permissions

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel
    @Inject
    constructor(
        private val permissionManager: PermissionManager,
        private val permissionChecker: PermissionChecker,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PermissionUIState())
        val uiState: StateFlow<PermissionUIState> = _uiState.asStateFlow()

        init {
            checkPermissions()
        }

        fun checkPermissions() {
            viewModelScope.launch {
                val status = permissionChecker.getPermissionStatus()
                _uiState.value =
                    PermissionUIState(
                        hasUsageStats = status.usageAccess,
                        hasLocation = status.location,
                        hasBackgroundLocation = status.backgroundLocation,
                        hasActivityRecognition = status.activityRecognition,
                        hasNotifications = status.notifications,
                        hasAllRequired = status.allRequired,
                        hasAllOptional = status.allOptional,
                    )
            }
        }

        fun createUsageStatsIntent(): Intent = permissionManager.createUsageStatsIntent()

        fun createAppSettingsIntent(): Intent = permissionManager.createAppSettingsIntent()

        fun getRuntimePermissionsToRequest(): List<String> = permissionChecker.getRuntimePermissionsToRequest()

        fun getOptionalPermissionsToRequest(): List<String> = permissionChecker.getOptionalPermissionsToRequest()
    }

data class PermissionUIState(
    val hasUsageStats: Boolean = false,
    val hasLocation: Boolean = false,
    val hasBackgroundLocation: Boolean = false,
    val hasActivityRecognition: Boolean = false,
    val hasNotifications: Boolean = false,
    val hasAllRequired: Boolean = false,
    val hasAllOptional: Boolean = false,
) {
    val hasAllPermissions: Boolean
        get() = hasAllRequired
}
