package com.barryburgle.gameapp.ui.input.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.barryburgle.gameapp.event.AbstractSessionEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: InputState, onEvent: (AbstractSessionEvent) -> Unit, navController: NavController
) {
    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Tools Screen")
        }
    }
}