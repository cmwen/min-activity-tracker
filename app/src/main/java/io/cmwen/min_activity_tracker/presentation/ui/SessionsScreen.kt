package io.cmwen.min_activity_tracker.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import java.util.Date
import java.util.Locale

@Composable
fun SessionsScreen(viewModel: SessionsViewModel) {
    val sessions = viewModel.sessions.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "App Sessions",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
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
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "No Sessions Recorded",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start using your device to see app usage sessions here.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SessionList(sessions: List<AppSessionEntity>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = session.appLabel,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = session.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatDuration(session.durationMs),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = formatTimestamp(session.startTimestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val seconds = durationMs / MILLIS_IN_SECOND
    val minutes = seconds / SECONDS_IN_MINUTE
    val hours = minutes / MINUTES_IN_HOUR

    return when {
        hours > 0 -> "${hours}h ${minutes % MINUTES_IN_HOUR}m"
        minutes > 0 -> "${minutes}m ${seconds % SECONDS_IN_MINUTE}s"
        else -> "${seconds}s"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

private const val MILLIS_IN_SECOND = 1000L
private const val SECONDS_IN_MINUTE = 60
private const val MINUTES_IN_HOUR = 60
private const val ONE_HOUR_MILLIS = 3_600_000L

@Preview(showBackground = true)
@Composable
fun SessionRowPreview() {
    MinactivitytrackerTheme {
        SessionRow(
            AppSessionEntity(
                id = "preview",
                packageName = "com.example.app",
                appLabel = "Example App",
                startTimestamp = System.currentTimeMillis() - ONE_HOUR_MILLIS, // 1 hour ago
                endTimestamp = System.currentTimeMillis(),
                durationMs = ONE_HOUR_MILLIS, // 1 hour
            ),
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
