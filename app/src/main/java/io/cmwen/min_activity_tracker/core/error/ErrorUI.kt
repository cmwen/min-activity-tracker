package io.cmwen.min_activity_tracker.core.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme

/**
 * Error display components for showing user-friendly error messages
 */

@Composable
fun ErrorSnackbar(
    error: AppError,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        action = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    ) {
    Text(text = error.getUserMessage())
    }
}

@Composable
fun ErrorCard(
    error: AppError,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = error.getIcon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = error.getUserMessage(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (onRetry != null || onDismiss != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (onDismiss != null) {
                        TextButton(onClick = onDismiss) {
                            Text("Dismiss")
                        }
                    }
                    if (onRetry != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = error.getIcon(),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = error.getUserMessage(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

/**
 * Extension functions for AppError
 */
private fun AppError.getUserMessage(): String = when (this) {
    is AppError.DatabaseError.ConnectionError -> "Database connection failed. Please try again."
    is AppError.DatabaseError.CorruptionError -> "Data corruption detected. The app may need to reset its data."
    is AppError.DatabaseError.MigrationError -> "Database upgrade failed. Please restart the app."
    is AppError.PermissionError.UsageStatsNotGranted -> "Usage stats permission is required to track app usage."
    is AppError.PermissionError.LocationPermissionDenied -> "Location permission is required for location tracking."
    is AppError.PermissionError.NotificationAccessDenied -> "Notification access is required to monitor notifications."
    is AppError.DataCollectionError.ServiceUnavailable -> "Service is currently unavailable."
    is AppError.DataCollectionError.DataCorrupted -> "Data corruption detected."
    is AppError.UnknownError -> this.msg
}

private fun AppError.getIcon(): ImageVector = when (this) {
    is AppError.DatabaseError -> Icons.Filled.Error
    is AppError.PermissionError -> Icons.Filled.Warning
    is AppError.DataCollectionError -> Icons.Filled.Warning
    is AppError.UnknownError -> Icons.Filled.Error
}

@Preview(showBackground = true)
@Composable
fun ErrorCardPreview() {
    MinactivitytrackerTheme {
        ErrorCard(
            error = AppError.PermissionError.UsageStatsNotGranted,
            onRetry = { },
            onDismiss = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MinactivitytrackerTheme {
        ErrorScreen(
            error = AppError.DatabaseError.ConnectionError(RuntimeException("Connection failed")),
            onRetry = { }
        )
    }
}