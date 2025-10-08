package io.cmwen.min_activity_tracker.features.permissions

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PermissionsScreen(
    viewModel: PermissionViewModel = hiltViewModel(),
    onPermissionsGranted: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val requestPermissionsLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {
            viewModel.checkPermissions()
        }

    val openAppSettingsLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            viewModel.checkPermissions()
        }

    LaunchedEffect(uiState.hasAllPermissions) {
        if (uiState.hasAllPermissions) {
            onPermissionsGranted()
        }
    }

    Scaffold { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Permissions Required",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This app needs a few permissions to track your activity accurately. Please grant them to continue.",
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = viewModel.createUsageStatsIntent()
                    openAppSettingsLauncher.launch(intent)
                },
                enabled = !uiState.hasUsageStats,
            ) {
                Text("Grant Usage Stats Access")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    requestPermissionsLauncher.launch(PermissionManager.allPermissions.toTypedArray())
                },
            ) {
                Text("Grant Other Permissions")
            }
        }
    }
}
