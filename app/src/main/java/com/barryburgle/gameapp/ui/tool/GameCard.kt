package com.barryburgle.gameapp.ui.tool

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun GameCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    GenericSettingsCard("Game", modifier) {
        SwitchSetting(
            "Show period summary card", state.showSummaryCard
        ) {
            onEvent(ToolEvent.SwitchShowSummaryCard)
        }
        SwitchSetting(
            "Show ongoing challenge card on top", state.showOngoingChallengeCardOnTop
        ) {
            onEvent(ToolEvent.SwitchShowOngoingChallengeCardOnTop)
        }
    }
}