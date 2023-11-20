package com.barryburgle.gameapp.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
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
import com.barryburgle.gameapp.ui.tool.ToolsScreen
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    inputState: InputState,
    outputState: OutputState,
    toolState: ToolsState,
    inputOnEvent: (AbstractSessionEvent) -> Unit,
    outputOnEvent: (ChartTypeEvent) -> Unit,
    toolOnEvent: (ToolEvent) -> Unit
) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavigationItem(
            title = "Input",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            hasNews = false,
            destinationScreen = Screen.InputScreen.route
        ),
        BottomNavigationItem(
            title = "Output",
            selectedIcon = Icons.Filled.CheckCircle,
            unselectedIcon = Icons.Outlined.CheckCircle,
            hasNews = false,
            destinationScreen = Screen.OutputScreen.route
        ),
        BottomNavigationItem(
            title = "Tools",
            selectedIcon = Icons.Filled.Build,
            unselectedIcon = Icons.Outlined.Build,
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
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.destinationScreen)
                        },
                        label = { Text(text = item.title) },
                        icon = {
                            BadgedBox(badge = {
                                if (item.badgeCount != null) {
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                } else if (item.hasNews) {
                                    Badge()
                                }
                            }) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    }, contentDescription = item.title
                                )
                            }
                        })
                }
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
            composable(Screen.ToolScreen.route) {
                ToolsScreen(state = toolState, onEvent = toolOnEvent)
            }
        }
    }
}