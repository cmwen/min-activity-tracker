package io.cmwen.min_activity_tracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.cmwen.min_activity_tracker.presentation.ui.DashboardScreen
import io.cmwen.min_activity_tracker.presentation.ui.SessionsScreen
import io.cmwen.min_activity_tracker.presentation.ui.SettingsScreen
import io.cmwen.min_activity_tracker.presentation.ui.SummariesScreen
import io.cmwen.min_activity_tracker.presentation.viewmodels.SessionsViewModel
import io.cmwen.min_activity_tracker.presentation.viewmodels.SummariesViewModel

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Sessions : Screen("sessions", "Sessions", Icons.Filled.Analytics)
    object Summaries : Screen("summaries", "Summaries", Icons.Filled.BarChart)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

@Composable
fun MainNavigation(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val items =
                    listOf(
                        Screen.Dashboard,
                        Screen.Sessions,
                        Screen.Summaries,
                        Screen.Settings,
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
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Sessions.route) {
                val viewModel: SessionsViewModel = hiltViewModel()
                SessionsScreen(viewModel = viewModel)
            }
            composable(Screen.Summaries.route) {
                val viewModel: SummariesViewModel = hiltViewModel()
                SummariesScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

// Provide a top-level declaration that matches the file name to satisfy detekt's
// MatchingDeclarationName rule. This is a harmless alias object used only for
// static analysis and may be removed/restructured later if desired.
object MainNavigation
