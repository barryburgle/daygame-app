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
            "Show current week summary", state.showCurrentWeekSummary
        ) {
            onEvent(ToolEvent.SwitchShowCurrentWeekSummary)
        }
        SwitchSetting(
            "Show current month summary", state.showCurrentMonthSummary
        ) {
            onEvent(ToolEvent.SwitchShowCurrentMonthSummary)
        }
    }
}