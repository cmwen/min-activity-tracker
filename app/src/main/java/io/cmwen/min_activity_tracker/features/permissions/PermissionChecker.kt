package io.cmwen.min_activity_tracker.features.permissions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized permission checking for all required and optional permissions
 */
@Singleton
class PermissionChecker
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        /**
         * Check if Usage Access permission is granted
         * Required for tracking app usage
         */
        fun hasUsageAccessPermission(): Boolean =
            try {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        appOps.unsafeCheckOpNoThrow(
                            AppOpsManager.OPSTR_GET_USAGE_STATS,
                            Process.myUid(),
                            context.packageName,
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        appOps.checkOpNoThrow(
                            AppOpsManager.OPSTR_GET_USAGE_STATS,
                            Process.myUid(),
                            context.packageName,
                        )
                    }
                mode == AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                false
            }

        /**
         * Check if location permission is granted
         */
        fun hasLocationPermission(): Boolean =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED

        /**
         * Check if background location permission is granted (Android 10+)
         */
        fun hasBackgroundLocationPermission(): Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                // Background location not needed on Android 9 and below
                true
            }

        /**
         * Check if activity recognition permission is granted
         */
        fun hasActivityRecognitionPermission(): Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                // Not required on Android 9 and below
                true
            }

        /**
         * Check if notification permission is granted (Android 13+)
         */
        fun hasNotificationPermission(): Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                // Not required below Android 13
                true
            }

        /**
         * Check if all required permissions are granted
         */
        fun hasAllRequiredPermissions(): Boolean = hasUsageAccessPermission() && hasNotificationPermission()

        /**
         * Check if all optional permissions are granted
         */
        fun hasAllOptionalPermissions(): Boolean =
            hasLocationPermission() &&
                hasBackgroundLocationPermission() &&
                hasActivityRecognitionPermission()

        /**
         * Get list of permissions needed for runtime request
         */
        fun getRuntimePermissionsToRequest(): List<String> {
            val permissions = mutableListOf<String>()

            if (!hasNotificationPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }

            return permissions
        }

        /**
         * Get list of optional permissions to request
         */
        fun getOptionalPermissionsToRequest(): List<String> {
            val permissions = mutableListOf<String>()

            if (!hasLocationPermission()) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (!hasActivityRecognitionPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
            }

            return permissions
        }

        /**
         * Get permission status summary
         */
        fun getPermissionStatus(): PermissionStatus =
            PermissionStatus(
                usageAccess = hasUsageAccessPermission(),
                location = hasLocationPermission(),
                backgroundLocation = hasBackgroundLocationPermission(),
                activityRecognition = hasActivityRecognitionPermission(),
                notifications = hasNotificationPermission(),
            )
    }

data class PermissionStatus(
    val usageAccess: Boolean,
    val location: Boolean,
    val backgroundLocation: Boolean,
    val activityRecognition: Boolean,
    val notifications: Boolean,
) {
    val allRequired: Boolean get() = usageAccess && notifications
    val allOptional: Boolean get() = location && backgroundLocation && activityRecognition
}
