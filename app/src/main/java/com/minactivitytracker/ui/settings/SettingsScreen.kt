package com.minactivitytracker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.minactivitytracker.repository.AutoExportInterval

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isUsageTrackingEnabled by viewModel.isUsageTrackingEnabled.collectAsState()
    val isBatteryTrackingEnabled by viewModel.isBatteryTrackingEnabled.collectAsState()
    val autoExportInterval by viewModel.autoExportInterval.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Data Collection",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSwitch(
            title = "Track App Usage",
            checked = isUsageTrackingEnabled,
            onCheckedChange = viewModel::setUsageTrackingEnabled
        )

        SettingsSwitch(
            title = "Track Battery",
            checked = isBatteryTrackingEnabled,
            onCheckedChange = viewModel::setBatteryTrackingEnabled
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Scheduled Export",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        AutoExportInterval.values().forEach { interval ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (interval == autoExportInterval),
                    onClick = { viewModel.setAutoExportInterval(interval) }
                )
                Text(
                    text = when (interval) {
                        AutoExportInterval.NONE -> "None"
                        AutoExportInterval.DAILY -> "Daily"
                        AutoExportInterval.WEEKLY -> "Weekly"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
