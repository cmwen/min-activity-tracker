package io.cmwen.min_activity_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.cmwen.min_activity_tracker.features.permissions.PermissionManager
import io.cmwen.min_activity_tracker.features.permissions.PermissionsScreen
import io.cmwen.min_activity_tracker.presentation.navigation.MainNavigation
import io.cmwen.min_activity_tracker.presentation.ui.SessionRow
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MinactivitytrackerTheme {
                var hasPermissions by remember { mutableStateOf(permissionManager.hasAllPermissions()) }

                if (hasPermissions) {
                    LaunchedEffect(Unit) {
                        startService(Intent(this@MainActivity, io.cmwen.min_activity_tracker.features.tracking.TrackingService::class.java))
                    }
                    MainNavigation()
                } else {
                    PermissionsScreen(onPermissionsGranted = { hasPermissions = true })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun mainPreview() {
    MinactivitytrackerTheme {
        // Preview uses a placeholder session entity
        SessionRow(
            AppSessionEntity(
                id = "preview",
                packageName = "io.preview.app",
                appLabel = "Preview App",
                startTimestamp = 0L,
                endTimestamp = 1L,
                durationMs = 1L,
            ),
        )
    }
}
