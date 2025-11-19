package com.minactivitytracker.ui

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AppList : Screen("app_list")
    data object Export : Screen("export")
    data object Settings : Screen("settings")
}
