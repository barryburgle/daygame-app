package com.barryburgle.gameapp.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.event.StatsEvent
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
@Composable
fun Navigation(
    inputState: InputState,
    outputState: OutputState,
    statsState: StatsState,
    toolState: ToolsState,
    inputOnEvent: (GameEvent) -> Unit,
    outputOnEvent: (OutputEvent) -> Unit,
    statsOnEvent: (StatsEvent) -> Unit,
    toolOnEvent: (ToolEvent) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(
        BottomNavigationItem(
            title = "Game",
            icon = painterResource(R.drawable.dice),
            modifier = Modifier.size(22.dp),
            hasNews = false,
            destinationScreen = Screen.InputScreen.route
        ),
        BottomNavigationItem(
            title = "Dashboard",
            icon = painterResource(R.drawable.chart),
            modifier = Modifier.size(22.dp),
            hasNews = false,
            destinationScreen = Screen.OutputScreen.route
        ),
        BottomNavigationItem(
            title = "Results",
            icon = painterResource(R.drawable.badge),
            modifier = Modifier.size(22.dp),
            hasNews = false,
            destinationScreen = Screen.StatsScreen.route
        ),
        BottomNavigationItem(
            title = "Settings",
            icon = painterResource(R.drawable.settings),
            modifier = Modifier.size(22.dp),
            hasNews = toolState.latestAvailable != null && toolState.latestAvailable.isNotEmpty(),
            destinationScreen = Screen.ToolScreen.route
        )
    )

    Scaffold(
        bottomBar = {
            androidx.compose.material3.Surface(
                modifier = Modifier
                    .padding(start = 34.dp, end = 34.dp, bottom = 18.dp)
                    .navigationBarsPadding(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.secondary,
                tonalElevation = 8.dp,
                shadowElevation = 12.dp
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    items.forEach { item ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == item.destinationScreen } == true
                        val selectedColor =
                            if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(item.destinationScreen) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
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
                                        painter = item.icon,
                                        contentDescription = item.title,
                                        tint = selectedColor,
                                        modifier = item.modifier
                                    )
                                }
                            })
                    }
                }
            }
        }
    ) { padding ->
        val spaceFromLeft = 16.dp
        val spaceFromTop = 20.dp
        val spaceFromBottom = 60.dp
        NavHost(navController = navController, startDestination = Screen.InputScreen.route) {
            composable(
                route = Screen.InputScreen.route,
                enterTransition = {
                    screenEnterTransition()
                },
                exitTransition = {
                    screenExitTransition()
                }
            ) {
                InputScreen(
                    state = inputState,
                    onEvent = inputOnEvent,
                    spaceFromLeft = spaceFromLeft,
                    spaceFromTop = spaceFromTop,
                    spaceFromBottom = spaceFromBottom
                )
            }
            composable(
                route = Screen.OutputScreen.route,
                enterTransition = {
                    screenEnterTransition()
                },
                exitTransition = {
                    screenExitTransition()
                }
            ) {
                OutputScreen(
                    state = outputState,
                    onEvent = outputOnEvent,
                    spaceFromLeft = spaceFromLeft,
                    spaceFromTop = spaceFromTop,
                    spaceFromBottom = spaceFromBottom
                )
            }
            composable(
                route = Screen.StatsScreen.route,
                enterTransition = {
                    screenEnterTransition()
                },
                exitTransition = {
                    screenExitTransition()
                }
            ) {
                StatsScreen(
                    state = statsState,
                    spaceFromLeft = spaceFromLeft,
                    spaceFromTop = spaceFromTop,
                    spaceFromBottom = spaceFromBottom,
                    onEvent = statsOnEvent
                )
            }
            composable(
                route = Screen.ToolScreen.route,
                enterTransition = {
                    screenEnterTransition()
                },
                exitTransition = {
                    screenExitTransition()
                }
            ) {
                ToolsScreen(
                    state = toolState,
                    onEvent = toolOnEvent,
                    spaceFromLeft = spaceFromLeft,
                    spaceFromTop = spaceFromTop,
                    spaceFromBottom = spaceFromBottom
                )
            }
        }
    }
}

private fun screenEnterTransition() = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(durationMillis = 250)
)

private fun screenExitTransition() =
    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut(
        animationSpec = tween(durationMillis = 150)
    )