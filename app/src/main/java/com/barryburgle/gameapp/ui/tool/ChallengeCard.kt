package com.barryburgle.gameapp.ui.tool

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun ChallengeCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    GenericSettingsCard("Challenge", modifier) {
        CountSetting(
            text = "Increment challenge goal by ${state.incrementChallengeGoal} units",
            count = state.incrementChallengeGoal,
            description = "Every time you increase the Challenge goal it will increment by ${state.incrementChallengeGoal}",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetIncrementChallengeGoal
        )
        SwitchSetting(
            "Show ongoing challenge card on top", state.showOngoingChallengeCardOnTop
        ) {
            onEvent(ToolEvent.SwitchShowOngoingChallengeCardOnTop)
        }
        CountSetting(
            text = "Start a new challenge with goal set to ${state.defaultChallengeGoal}",
            count = state.defaultChallengeGoal,
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetDefaultChallengeGoal
        )
    }
}