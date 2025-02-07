package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: ToolsState,
    onEvent: (ToolEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    Scaffold { padding ->
        val dataExchangeCardModifier = Modifier
            .height(370.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        val settingsCardModifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop + spaceFromLeft
                ),
            verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Export",
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Import",
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    SettingsCard(
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)
                )
            }
        }
    }
}
