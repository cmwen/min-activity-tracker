package io.cmwen.min_activity_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.cmwen.min_activity_tracker.presentation.ui.SessionsScreen
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModel
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModelFactory
import io.cmwen.min_activity_tracker.data.database.MinActivityDatabase
import io.cmwen.min_activity_tracker.data.repository.SessionRepositoryImpl
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Manual wiring for now (replace with DI when Hilt is enabled)
        val db = MinActivityDatabase::class.java // placeholder to avoid static init in onCreate

        setContent {
            MinactivitytrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // In a real app we'd obtain repo via DI. Use preview VM in UI preview.
                    val placeholderRepo = SessionRepositoryImpl(
                        // create an in-memory DB or mock later; for now use DAO from Room via builder if needed
                        // ... keep as a TODO for integration
                        dao = object : io.cmwen.min_activity_tracker.data.database.SessionDao {
                            override fun getAllSessions(): kotlinx.coroutines.flow.Flow<List<io.cmwen.min_activity_tracker.data.database.AppSessionEntity>> =
                                kotlinx.coroutines.flow.flowOf(emptyList())
                            override suspend fun insert(session: io.cmwen.min_activity_tracker.data.database.AppSessionEntity) {}
                            override suspend fun deleteById(id: String) {}
                        }
                    )
                    val vm = SessionsViewModelFactory(placeholderRepo).create()
                    SessionsScreen(viewModel = vm)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MinactivitytrackerTheme {
        // Preview uses a placeholder view model in previews generally; keep simple
        io.cmwen.min_activity_tracker.presentation.ui.SessionRow(io.cmwen.min_activity_tracker.data.database.AppSessionEntity(
            id = "preview",
            packageName = "io.preview.app",
            appLabel = "Preview App",
            startTimestamp = 0L,
            endTimestamp = 1L,
            durationMs = 1L
        ))
    }
}