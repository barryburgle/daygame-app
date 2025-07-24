package com.barryburgle.gameapp.ui.tool

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun ShareCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    GenericSettingsCard("Share", modifier) {
        SwitchSetting(
            "Share simpler report for +1s", state.simplePlusOneReport,
            description = "Report without sticking points and date flags is generated for dates that ended up in a lay"
        ) {
            onEvent(ToolEvent.SwitchSimplePlusOneReport)
        }
        SwitchSetting(
            "Never share lead info", state.neverShareLeadInfo
        ) {
            onEvent(ToolEvent.SwitchNeverShareLeadInfo)
        }
    }
}