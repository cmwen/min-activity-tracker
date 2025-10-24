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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.cmwen.min_activity_tracker.data.preferences.UserPreferences
import io.cmwen.min_activity_tracker.presentation.viewmodels.ExportFormat
import io.cmwen.min_activity_tracker.presentation.viewmodels.ExportState
import io.cmwen.min_activity_tracker.presentation.viewmodels.ExportTimeRange
import io.cmwen.min_activity_tracker.presentation.viewmodels.SettingsUiState
import io.cmwen.min_activity_tracker.presentation.viewmodels.SettingsViewModel
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showExportDialog by remember { mutableStateOf(false) }

    ExportStateObserver(
        exportState = uiState.exportState,
        snackbarHostState = snackbarHostState,
        onDismiss = viewModel::resetExportState,
    )

    SettingsContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        showExportDialog = showExportDialog,
        onShowExportDialog = { showExportDialog = true },
        onDismissExportDialog = { showExportDialog = false },
        onExportRequested = { format, anonymize, range ->
            viewModel.exportData(format, anonymize, range)
            showExportDialog = false
        },
        onAppUsageToggled = viewModel::onAppUsageTrackingChanged,
        onBatteryToggled = viewModel::onBatteryTrackingChanged,
        onLocationToggled = viewModel::onLocationTrackingChanged,
        onActivityRecognitionToggled = viewModel::onActivityRecognitionChanged,
        onAnonymizeLocationToggled = viewModel::onAnonymizeLocationChanged,
        onClearLocationData = viewModel::clearSavedLocationData,
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    snackbarHostState: SnackbarHostState,
    showExportDialog: Boolean,
    onShowExportDialog: () -> Unit,
    onDismissExportDialog: () -> Unit,
    onExportRequested: (ExportFormat, Boolean, ExportTimeRange) -> Unit,
    onAppUsageToggled: (Boolean) -> Unit,
    onBatteryToggled: (Boolean) -> Unit,
    onLocationToggled: (Boolean) -> Unit,
    onActivityRecognitionToggled: (Boolean) -> Unit,
    onAnonymizeLocationToggled: (Boolean) -> Unit,
    onClearLocationData: () -> Unit,
) {
    val prefs = uiState.preferences

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            SettingsSection(title = "Data Collection") {
                SettingsToggleItem(
                    title = "App Usage Tracking",
                    description = "Track which apps you use and for how long",
                    isChecked = prefs.collectAppUsage,
                    onToggle = onAppUsageToggled,
                )

                SettingsToggleItem(
                    title = "Battery Monitoring",
                    description = "Monitor battery level and charging state",
                    isChecked = prefs.collectBattery,
                    onToggle = onBatteryToggled,
                )

                SettingsToggleItem(
                    title = "Location Tracking",
                    description = "Record approximate location data (optional)",
                    isChecked = prefs.collectLocation,
                    onToggle = onLocationToggled,
                )

                SettingsToggleItem(
                    title = "Activity Recognition",
                    description = "Detect driving, walking, or still activity (requires permission)",
                    isChecked = prefs.collectActivityRecognition,
                    onToggle = onActivityRecognitionToggled,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "Privacy") {
                SettingsToggleItem(
                    title = "Anonymize Location Data",
                    description = "Remove precise location before storing or exporting data",
                    isChecked = prefs.anonymizeLocationData,
                    onToggle = onAnonymizeLocationToggled,
                )

                SettingsClickableItem(
                    title = "Clear Saved Location History",
                    description = "Remove stored latitude and longitude from past sessions",
                    onClick = onClearLocationData,
                )

                SettingsClickableItem(
                    title = "Export Data",
                    description = "Generate JSON or CSV exports for analysis",
                    onClick = onShowExportDialog,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "About") {
                SettingsClickableItem(
                    title = "Version",
                    description = "1.0.0",
                    onClick = { },
                )
            }
        }
    }

    if (showExportDialog) {
        ExportDialog(
            preferences = prefs,
            onDismiss = onDismissExportDialog,
            onExport = onExportRequested,
        )
    }
}

@Composable
private fun ExportDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onExport: (ExportFormat, Boolean, ExportTimeRange) -> Unit,
) {
    var selectedFormat by remember { mutableStateOf(ExportFormat.JSON) }
    var selectedRange by remember { mutableStateOf(ExportTimeRange.LAST_24_HOURS) }
    var anonymize by remember { mutableStateOf(preferences.anonymizeLocationData) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Data") },
        text = {
            Column {
                Text(
                    text = "Choose the export format and time range.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                ExportOption(
                    label = "Format",
                    options = ExportFormat.entries.toList(),
                    selected = selectedFormat,
                    onSelected = { selectedFormat = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExportOption(
                    label = "Time Range",
                    options = ExportTimeRange.entries.toList(),
                    selected = selectedRange,
                    onSelected = { selectedRange = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingsToggleItem(
                    title = "Anonymize Location Data",
                    description = "Remove location columns from this export",
                    isChecked = anonymize,
                    onToggle = { anonymize = it },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onExport(selectedFormat, anonymize, selectedRange) },
            ) {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Enum<T>> ExportOption(
    label: String,
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        options.forEach { option ->
            androidx.compose.material3.ListItem(
                headlineContent = { Text(option.name.replace('_', ' ')) },
                leadingContent = {
                    androidx.compose.material3.RadioButton(
                        selected = option == selected,
                        onClick = { onSelected(option) },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ExportStateObserver(
    exportState: ExportState,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(exportState) {
        when (exportState) {
            ExportState.Idle -> Unit
            ExportState.InProgress -> Unit
            is ExportState.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Exported to ${exportState.file.name}",
                    duration = SnackbarDuration.Short,
                )
                onDismiss()
            }
            is ExportState.Error -> {
                snackbarHostState.showSnackbar(
                    message = exportState.throwable.localizedMessage ?: "Export failed",
                    duration = SnackbarDuration.Short,
                )
                onDismiss()
            }
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
private fun SettingsScreenPreview() {
    MinactivitytrackerTheme {
        SettingsContent(
            uiState =
                SettingsUiState(
                    preferences =
                        UserPreferences(
                            collectAppUsage = true,
                            collectBattery = true,
                            collectLocation = true,
                            collectActivityRecognition = false,
                            anonymizeLocationData = false,
                        ),
                ),
            snackbarHostState = remember { SnackbarHostState() },
            showExportDialog = false,
            onShowExportDialog = {},
            onDismissExportDialog = {},
            onExportRequested = { _, _, _ -> },
            onAppUsageToggled = {},
            onBatteryToggled = {},
            onLocationToggled = {},
            onActivityRecognitionToggled = {},
            onAnonymizeLocationToggled = {},
            onClearLocationData = {},
        )
    }
}
