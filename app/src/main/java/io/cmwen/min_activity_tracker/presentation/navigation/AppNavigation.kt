package io.cmwen.min_activity_tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.cmwen.min_activity_tracker.presentation.screens.dashboard.DashboardScreen
import io.cmwen.min_activity_tracker.presentation.screens.permissions.PermissionsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToPermissions = { navController.navigate("permissions") }
            )
        }
        composable("permissions") {
            PermissionsScreen(
                onPermissionsGranted = { navController.popBackStack() }
            )
        }
    }
}
