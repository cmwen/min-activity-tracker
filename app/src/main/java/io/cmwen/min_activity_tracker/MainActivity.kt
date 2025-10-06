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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.cmwen.min_activity_tracker.features.permissions.PermissionChecker
import io.cmwen.min_activity_tracker.features.permissions.PermissionsScreen
import io.cmwen.min_activity_tracker.features.workers.WorkScheduler
import io.cmwen.min_activity_tracker.presentation.navigation.MainNavigation
import io.cmwen.min_activity_tracker.presentation.ui.SessionRow
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionChecker: PermissionChecker
    
    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MinactivitytrackerTheme {
                var hasPermissions by remember { 
                    mutableStateOf(permissionChecker.hasAllRequiredPermissions()) 
                }

                LaunchedEffect(hasPermissions) {
                    if (hasPermissions) {
                        // Initialize background data collection
                        workScheduler.scheduleDataCollection()
                        workScheduler.scheduleDailyAnalysis()
                        workScheduler.scheduleWeeklyAnalysis()
                    }
                }

                if (hasPermissions) {
                    MainNavigation()
                } else {
                    PermissionsScreen(
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Recheck permissions when app resumes (user may have changed them in settings)
        setContent {
            MinactivitytrackerTheme {
                val hasPermissions = remember { 
                    mutableStateOf(permissionChecker.hasAllRequiredPermissions()) 
                }.value
                
                if (hasPermissions) {
                    MainNavigation()
                } else {
                    PermissionsScreen(
                        onNavigateBack = { finish() }
                    )
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
