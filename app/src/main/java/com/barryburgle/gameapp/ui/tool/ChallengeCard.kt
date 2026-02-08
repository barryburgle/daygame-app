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
        SwitchSetting(
            "Show ongoing challenge summary", state.showCurrentChallengeSummary
        ) {
            onEvent(ToolEvent.SwitchShowCurrentChallengeSummary)
        }
        CountSetting(
            text = "Increment challenge goal by ${state.incrementChallengeGoal}",
            count = state.incrementChallengeGoal,
            description = "The + button will increase challenge goal of ${state.incrementChallengeGoal}",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetIncrementChallengeGoal
        )
        CountSetting(
            text = "Start a new challenge with goal set to ${state.defaultChallengeGoal}",
            count = state.defaultChallengeGoal,
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetDefaultChallengeGoal
        )
    }
}