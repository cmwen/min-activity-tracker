package io.cmwen.min_activity_tracker.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModel
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionsScreen(viewModel: SessionsViewModel) {
    val sessions = viewModel.sessions.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "App Sessions",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (sessions.value.isEmpty()) {
            EmptySessionsMessage()
        } else {
            SessionList(sessions = sessions.value)
        }
    }
}

@Composable
private fun EmptySessionsMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Sessions Recorded",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start using your device to see app usage sessions here.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SessionList(sessions: List<AppSessionEntity>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sessions, key = { it.id }) { session ->
            SessionRow(session)
        }
    }
}

@Composable
fun SessionRow(session: AppSessionEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = session.appLabel,
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = session.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDuration(session.durationMs),
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = formatTimestamp(session.startTimestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val seconds = durationMs / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    
    return when {
        hours > 0 -> "${hours}h ${minutes % 60}m"
        minutes > 0 -> "${minutes}m ${seconds % 60}s"
        else -> "${seconds}s"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun SessionRowPreview() {
    MinactivitytrackerTheme {
        SessionRow(
            AppSessionEntity(
                id = "preview",
                packageName = "com.example.app",
                appLabel = "Example App",
                startTimestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                endTimestamp = System.currentTimeMillis(),
                durationMs = 3600000 // 1 hour
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptySessionsPreview() {
    MinactivitytrackerTheme {
        EmptySessionsMessage()
    }
}
