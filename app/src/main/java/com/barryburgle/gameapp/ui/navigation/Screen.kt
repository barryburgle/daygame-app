package com.barryburgle.gameapp.ui.navigation

sealed class Screen(val route: String) {
    object InputScreen : Screen("input")
    object OutputScreen : Screen("output")
    object ToolScreen : Screen("tool")
}
