package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: ToolsState,
    onEvent: (ToolEvent) -> Unit
) {
    val spaceFromTop = 20.dp
    val spaceFromBottom = 60.dp
    Scaffold { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ExportCard(
                    state = state,
                    onEvent = onEvent
                )
            }
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
        }
    }
}
