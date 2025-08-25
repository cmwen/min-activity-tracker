package io.cmwen.min_activity_tracker.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme

@Composable
fun SettingsScreen() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Data Collection Settings
        SettingsSection(title = "Data Collection") {
            SettingsToggleItem(
                title = "App Usage Tracking",
                description = "Track which apps you use and for how long",
                isChecked = false,
                onToggle = { /* TODO: Implement */ },
            )

            SettingsToggleItem(
                title = "Battery Monitoring",
                description = "Monitor battery level and charging state",
                isChecked = false,
                onToggle = { /* TODO: Implement */ },
            )

            SettingsToggleItem(
                title = "Location Tracking",
                description = "Record location data (optional)",
                isChecked = false,
                onToggle = { /* TODO: Implement */ },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy Settings
        SettingsSection(title = "Privacy") {
            SettingsClickableItem(
                title = "Data Export",
                description = "Export your data as JSON or CSV",
                onClick = { /* TODO: Implement */ },
            )

            SettingsClickableItem(
                title = "Clear Data",
                description = "Delete all collected data",
                onClick = { /* TODO: Implement */ },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // About
        SettingsSection(title = "About") {
            SettingsClickableItem(
                title = "Version",
                description = "1.0.0",
                onClick = { },
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            content()
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
        )
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MinactivitytrackerTheme {
        SettingsScreen()
    }
}
