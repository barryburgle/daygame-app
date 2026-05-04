package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun LiveSessionCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    GenericSettingsCard("Live Session", modifier) {
        SwitchSetting(
            "Live Session persistent notification", state.liveSessionNotificationEnabled,
            description = "Show persistent notification with set, conversation and contact buttons during Live Session"
        ) {
            onEvent(ToolEvent.SwitchLiveSessionNotification)
        }
        SwitchSetting(
            "Live Session sitting reminder", state.liveSessionSittingReminderEnabled,
            description = "Reminds you to have a sit every ${state.liveSessionSittingReminderInterval} minutes during Live Session"
        ) {
            onEvent(ToolEvent.SwitchLiveSessionSittingReminder)
        }
        Spacer(modifier = Modifier.width(5.dp))
        CountSetting(
            text = "Sitting reminder every ${state.liveSessionSittingReminderInterval} minutes",
            count = state.liveSessionSittingReminderInterval,
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetLiveSessionSittingReminderInterval
        )
        Spacer(modifier = Modifier.width(5.dp))
        SwitchSetting(
            "Share Live Session", state.liveSessionShareEnabled,
            description = "After starting Live Sessions copies a shareable tweet to clipboard and opens share menu. Useful for starting a thread on X to share your session"
        ) {
            onEvent(ToolEvent.SwitchLiveSessionShare)
        }
    }
}