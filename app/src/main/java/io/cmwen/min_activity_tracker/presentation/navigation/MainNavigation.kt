package io.cmwen.min_activity_tracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.cmwen.min_activity_tracker.di.AppContainer
import io.cmwen.min_activity_tracker.presentation.ui.DashboardScreen
import io.cmwen.min_activity_tracker.presentation.ui.SessionsScreen
import io.cmwen.min_activity_tracker.presentation.ui.SettingsScreen
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModelFactory

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Sessions : Screen("sessions", "Sessions", Icons.Filled.Analytics)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

@Composable
fun MainNavigation(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    Screen.Dashboard,
                    Screen.Sessions,
                    Screen.Settings
                )
                
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Sessions.route) {
                val viewModel = SessionsViewModelFactory(appContainer.sessionRepository).create()
                SessionsScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}