package io.cmwen.min_activity_tracker.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModel

@Composable
fun SessionsScreen(viewModel: SessionsViewModel) {
    val sessions = viewModel.sessions.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        SessionList(sessions = sessions.value)
    }
}

@Composable
fun SessionList(sessions: List<AppSessionEntity>) {
    LazyColumn {
        items(sessions, key = { it.id }) { s ->
            SessionRow(s)
        }
    }
}

@Composable
fun SessionRow(session: AppSessionEntity) {
    Card {
        Column {
            Text(text = session.appLabel)
            Text(text = "${session.durationMs} ms")
        }
    }
}
