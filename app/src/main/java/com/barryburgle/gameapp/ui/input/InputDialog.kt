package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddInputDialog(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    MaterialDialog(dialogState = dateDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }) {
        this.datepicker(
            initialDate = LocalDate.now(),
            title = "Set date"
        ) {
            onEvent(AbstractSessionEvent.SetDate(it.toString()))
        }
    }
    MaterialDialog(dialogState = startHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set start hour"
        ) {
            onEvent(AbstractSessionEvent.SetStartHour(it.toString().substring(0, 5)))
        }
    }
    MaterialDialog(dialogState = endHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set end hour",
        ) {
            onEvent(AbstractSessionEvent.SetEndHour(it.toString().substring(0, 5)))
        }
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AbstractSessionEvent.HideDialog)
        },
        title = {
            Text(text = "Add session")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { dateDialogState.show() }) {
                    Text(text = "Set date")
                }
                Button(onClick = { startHourDialogState.show() }) {
                    Text(text = "Set start time")
                }
                Button(onClick = { endHourDialogState.show() }) {
                    Text(text = "Set end time")
                }
                TextField(value = state.sets,
                    onValueChange = { onEvent(AbstractSessionEvent.SetSets(it)) },
                    placeholder = { Text(text = "Sets") })
                TextField(value = state.convos,
                    onValueChange = { onEvent(AbstractSessionEvent.SetConvos(it)) },
                    placeholder = { Text(text = "Convos") })
                TextField(value = state.contacts,
                    onValueChange = { onEvent(AbstractSessionEvent.SetContacts(it)) },
                    placeholder = { Text(text = "Contacts") })
                TextField(value = state.stickingPoints,
                    onValueChange = { onEvent(AbstractSessionEvent.SetStickingPoints(it)) },
                    placeholder = { Text(text = "Sticking Points") })
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(AbstractSessionEvent.SaveAbstractSession)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}