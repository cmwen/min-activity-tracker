package io.cmwen.min_activity_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.cmwen.min_activity_tracker.presentation.navigation.MainNavigation
import io.cmwen.min_activity_tracker.presentation.ui.SessionRow
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get dependency container from Application
        val appContainer = (application as MinActivityApplication).container

        setContent {
            MinactivitytrackerTheme {
                MainNavigation(appContainer = appContainer)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MinactivitytrackerTheme {
        // Preview uses a placeholder session entity
        SessionRow(
            AppSessionEntity(
                id = "preview",
                packageName = "io.preview.app",
                appLabel = "Preview App",
                startTimestamp = 0L,
                endTimestamp = 1L,
                durationMs = 1L
            )
        )
    }
}