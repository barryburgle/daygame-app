package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.TimeInputFormEnum
import com.barryburgle.gameapp.ui.input.state.InputState

@Composable
fun DialogTimeFormSection(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    latestDateValue: String,
    latestStartHour: String,
    latestEndHour: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(135.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        TimeInputFormButton(
            state.date,
            latestDateValue,
            TimeInputFormEnum.DATE,
            state.isAddingDate,
            state.editDate,
            if (state.editDate != null && state.editDate!!.date != null) state.editDate!!.date!! else "",
            "session",
            ""
        ) {
            onEvent(GameEvent.SetDate(it))
        }
        TimeInputFormButton(
            state.startHour,
            latestStartHour,
            TimeInputFormEnum.HOUR,
            state.isAddingDate,
            state.editDate,
            if (state.editDate != null) state.editDate!!.startHour else "",
            "session",
            "Start"
        ) {
            onEvent(GameEvent.SetStartHour(it.substring(0, 5)))
        }
        TimeInputFormButton(
            state.endHour,
            latestEndHour,
            TimeInputFormEnum.HOUR,
            state.isAddingDate,
            state.editDate,
            if (state.editDate != null) state.editDate!!.endHour else "",
            "session",
            "End"
        ) {
            onEvent(GameEvent.SetEndHour(it.substring(0, 5)))
        }
    }
}