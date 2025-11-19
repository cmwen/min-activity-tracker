package com.minactivitytracker.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AppList : Screen("app_list")
    object Export : Screen("export")
}
