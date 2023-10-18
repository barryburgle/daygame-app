package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
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
    MaterialDialog(
        dialogState = dateDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.datepicker(
            initialDate = LocalDate.now(),
            title = "Set date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            onEvent(AbstractSessionEvent.SetDate(it.toString()))
        }
    }
    MaterialDialog(
        dialogState = startHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set start hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                activeBackgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            onEvent(AbstractSessionEvent.SetStartHour(it.toString().substring(0, 5)))
        }
    }
    MaterialDialog(
        dialogState = endHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set end hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                activeBackgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            onEvent(AbstractSessionEvent.SetEndHour(it.toString().substring(0, 5)))
        }
    }
    AlertDialog(
        modifier = modifier.shadow(elevation = 10.dp),
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
                Button(
                    onClick = { dateDialogState.show() }) {
                    Text(text = "Set date")
                }
                Button(
                    onClick = { startHourDialogState.show() }) {
                    Text(text = "Set start hour")
                }
                Button(
                    onClick = { endHourDialogState.show() }) {
                    Text(text = "Set end hour")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InputCountComponent(
                        inputTitle = "Sets", modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        onEvent = onEvent,
                        saveEvent = AbstractSessionEvent::SetSets
                    )
                    InputCountComponent(
                        inputTitle = "Convos", modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        onEvent = onEvent,
                        saveEvent = AbstractSessionEvent::SetConvos
                    )
                    InputCountComponent(
                        inputTitle = "Contacts", modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        onEvent = onEvent,
                        saveEvent = AbstractSessionEvent::SetContacts
                    )
                }
                OutlinedTextField(
                    value = state.stickingPoints,
                    onValueChange = { onEvent(AbstractSessionEvent.SetStickingPoints(it)) },
                    placeholder = { Text(text = "Sticking Points") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(100.dp)
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        onEvent(AbstractSessionEvent.SaveAbstractSession)
                    }) {
                    Text(text = "Save")
                }
            }
        }
    )
}
