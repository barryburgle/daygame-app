package com.barryburgle.gameapp.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.ChartTypeEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.input.InputScreen
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.output.OutputScreen
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.stats.StatsScreen
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.tool.ToolsScreen
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    inputState: InputState,
    outputState: OutputState,
    statsState: StatsState,
    toolState: ToolsState,
    inputOnEvent: (AbstractSessionEvent) -> Unit,
    outputOnEvent: (ChartTypeEvent) -> Unit,
    toolOnEvent: (ToolEvent) -> Unit
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "Sessions",
            selectedIcon = Icons.Filled.EditNote,
            unselectedIcon = Icons.Outlined.EditNote,
            hasNews = false,
            destinationScreen = Screen.InputScreen.route
        ),
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.AutoMirrored.Filled.TrendingUp,
            unselectedIcon = Icons.AutoMirrored.Outlined.TrendingUp,
            hasNews = false,
            destinationScreen = Screen.OutputScreen.route
        ),
        BottomNavigationItem(
            title = "Results",
            selectedIcon = Icons.Filled.Check,
            unselectedIcon = Icons.Outlined.Check,
            hasNews = false,
            destinationScreen = Screen.StatsScreen.route
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = false,
            destinationScreen = Screen.ToolScreen.route
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { index, item ->
                    item.selected = selectedItemIndex == index
                    val selectedColor =
                        if (item.selected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
                    NavigationBarItem(
                        selected = item.selected,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.destinationScreen)
                        },
                        label = { Text(text = item.title, color = selectedColor) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(
                                                text = item.badgeCount.toString()
                                            )
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (item.selected) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title,
                                    tint = selectedColor
                                )
                            }
                        })
                }
                val pippo = false
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = Screen.InputScreen.route) {
            composable(Screen.InputScreen.route) {
                InputScreen(state = inputState, onEvent = inputOnEvent)
            }
            composable(Screen.OutputScreen.route) {
                OutputScreen(state = outputState, onEvent = outputOnEvent)
            }
            composable(Screen.StatsScreen.route) {
                StatsScreen(state = statsState)
            }
            composable(Screen.ToolScreen.route) {
                ToolsScreen(state = toolState, onEvent = toolOnEvent)
            }
        }
    }
}