package com.barryburgle.gameapp.ui.navigation

sealed class Screen(val route: String) {
    object InputScreen : Screen("sessions")
    object OutputScreen : Screen("dashboard")
    object DateScreen : Screen("date")
    object StatsScreen : Screen("stats")
    object ToolScreen : Screen("settings")
}
