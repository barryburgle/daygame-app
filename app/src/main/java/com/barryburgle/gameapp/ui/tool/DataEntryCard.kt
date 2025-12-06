package com.barryburgle.gameapp.ui.tool

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.IconButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun DataEntryCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    val notificationHourDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = notificationHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set notification hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(ToolEvent.SetNotificationTime(it.toString().substring(0, 5)))
        }
    }
    GenericSettingsCard("Data Entry", modifier) {
        IconButtonSetting(text = "Set sticking points reminder",
            imageVector = Icons.Default.Timer,
            contentDescription = "Reminder",
            onClick = { notificationHourDialogState.show() })
        SwitchSetting(
            "Generate iDate on set creation", state.generateiDate,
            description = "A new iDate will be generated when a set is inserted with the iDate flag set to true"
        ) {
            onEvent(ToolEvent.SwitchGenerateiDate)
        }
        SwitchSetting(
            "Follow count",
            state.followCount,
            description = "When inserting a session increase sets if conversations is increased, increase sets and conversations if contacts is increased, increase sets conversations and contacts if a new lead is inserted"
        ) {
            onEvent(ToolEvent.SwitchFollowCount)
        }
        SwitchSetting(
            "Suggest leads nationality",
            state.suggestLeadsNationality,
        ) {
            onEvent(ToolEvent.SwitchSuggestLeadsNationality)
        }
        SwitchSetting(
            "Auto-set date and time",
            state.autoSetEventDateTime,
            description = "When inserting a new session, set or date the date and time will respectively be set to the current one and the one selected by the respective toggle"
        ) {
            onEvent(ToolEvent.SwitchAutoSetEventDateTime)
        }
        var autoSetOptionTitle = "Auto-set the %s time"
        var autoSetOptionDesc = "Now setting the %s time of a new %s to current time"
        autoSetEventTime(
            autoSetOptionTitle,
            autoSetOptionDesc,
            EventTypeEnum.SESSION,
            state.autoSetSessionTimeToStart && state.autoSetEventDateTime, // Such that disabling state.autoSetEventDateTime makes all the other flags disabled, but the actual time settings are never deleted
            state.autoSetEventDateTime
        ) {
            onEvent(ToolEvent.SwitchAutoSetSessionTimeToStart)
        }
        autoSetEventTime(
            autoSetOptionTitle,
            autoSetOptionDesc,
            EventTypeEnum.SET,
            state.autoSetSetTimeToStart && state.autoSetEventDateTime, // Such that disabling state.autoSetEventDateTime makes all the other flags disabled, but the actual time settings are never deleted
            state.autoSetEventDateTime
        ) {
            onEvent(ToolEvent.SwitchAutoSetSetTimeToStart)
        }
        autoSetEventTime(
            autoSetOptionTitle,
            autoSetOptionDesc,
            EventTypeEnum.DATE,
            state.autoSetDateTimeToStart && state.autoSetEventDateTime, // Such that disabling state.autoSetEventDateTime makes all the other flags disabled, but the actual time settings are never deleted
            state.autoSetEventDateTime
        ) {
            onEvent(ToolEvent.SwitchAutoSetDateTimeToStart)
        }
    }
}

@Composable
fun autoSetEventTime(
    autoSetOptionTitle: String,
    autoSetOptionDesc: String,
    eventType: EventTypeEnum,
    flag: Boolean,
    autoSetEventDateTime: Boolean,
    onCheckedChange: () -> Unit
) {
    val actualSetting = if (flag) "start" else "end"
    val masterDisableMessage =
        eventType.getField() + " " + actualSetting + " time insert disabled: please enable the \"Auto-set date and time\" option"
    SwitchSetting(
        String.format(
            autoSetOptionTitle,
            eventType.getField().lowercase()
        ),
        flag,
        if (!autoSetEventDateTime) masterDisableMessage else
            String.format(
                autoSetOptionDesc,
                actualSetting,
                eventType.getField().lowercase()
            )
    ) {
        onCheckedChange()
    }
}