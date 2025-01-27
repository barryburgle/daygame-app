package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.state.InputState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
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
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    val dateDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    val descriptionFontSize = 13.sp
    val sessionTimeColumnWidth = 120.dp
    val sessionLeadColumnWidth = 140.dp
    val addLeadColumnWidth = 40.dp
    if (state.isUpdatingSession) {
        setState(state)
    }
    MaterialDialog(
        dialogState = dateDialogState, elevation = 10.dp, buttons = {
            positiveButton(
                "Ok", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }, shape = MaterialTheme.shapes.extraLarge
    ) {
        this.datepicker(
            initialDate = if (state.isAddingSession || state.editAbstractSession == null) LocalDate.now() else FormatService.parseDate(
                state.editAbstractSession.date
            ), title = "Set date", colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.onTertiary,
                dateInactiveTextColor = MaterialTheme.colorScheme.background

            )
        ) {
            onEvent(AbstractSessionEvent.SetDate(it.toString()))
        }
    }
    MaterialDialog(
        dialogState = startHourDialogState, elevation = 10.dp, buttons = {
            positiveButton(
                "Ok", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }, shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = if (state.isAddingSession || state.editAbstractSession == null) LocalTime.now() else FormatService.parseTime(
                state.editAbstractSession.startHour
            ), title = "Set start hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(AbstractSessionEvent.SetStartHour(it.toString().substring(0, 5)))
        }
    }
    MaterialDialog(
        dialogState = endHourDialogState, elevation = 10.dp, buttons = {
            positiveButton(
                "Ok", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }, shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = if (state.isAddingSession || state.editAbstractSession == null) LocalTime.now() else FormatService.parseTime(
                state.editAbstractSession.endHour
            ), title = "Set end hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(AbstractSessionEvent.SetEndHour(it.toString().substring(0, 5)))
        }
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(AbstractSessionEvent.HideDialog)
    }, title = {
        Text(text = description)
    }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(addLeadColumnWidth),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.width(sessionTimeColumnWidth)
                        ) {
                            formSectionDescription("Set session's:", descriptionFontSize)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier.width(sessionLeadColumnWidth - addLeadColumnWidth)
                        ) {
                            formSectionDescription("Leads:", descriptionFontSize)
                        }
                        Column(
                            modifier = Modifier.width(addLeadColumnWidth)
                        ) {
                            IconButton(
                                onClick = {
                                    /*onEvent(
                                        AbstractSessionEvent.EditSession(
                                            abstractSession
                                        )
                                    )
                                    onEvent(
                                        AbstractSessionEvent.ShowDialog(false, true)
                                    )*/
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add a lead",
                                    tint = MaterialTheme.colorScheme.inversePrimary,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.secondaryContainer
                                        )
                                        .width(addLeadColumnWidth)
                                        .height(addLeadColumnWidth)
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(sessionTimeColumnWidth)
                ) {
                    timeInputButton("Date", dateDialogState)
                    timeInputButton("Start hour", startHourDialogState)
                    timeInputButton("End hour", endHourDialogState)
                }
                Column(
                    modifier = Modifier.width(sessionLeadColumnWidth)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InputCountComponent(
                    inputTitle = "Sets",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.sets,
                    saveEvent = AbstractSessionEvent::SetSets
                )
                InputCountComponent(
                    inputTitle = "Conversations",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.convos,
                    saveEvent = AbstractSessionEvent::SetConvos
                )
                InputCountComponent(
                    inputTitle = "Contacts",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.contacts,
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
    }, confirmButton = {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.width(250.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onEvent(AbstractSessionEvent.HideDialog)
                    }
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        onEvent(AbstractSessionEvent.SaveAbstractSession)
                        onEvent(AbstractSessionEvent.HideDialog)
                        Toast.makeText(localContext, "Session saved", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    })
}

fun setState(
    state: InputState
) {
    if (state.editAbstractSession != null) {
        state.date = state.editAbstractSession.date.substring(0, 10)
        state.startHour = state.editAbstractSession.startHour.substring(11, 16)
        state.endHour = state.editAbstractSession.endHour.substring(11, 16)
        state.sets = state.editAbstractSession.sets.toString()
        state.convos = state.editAbstractSession.convos.toString()
        state.contacts = state.editAbstractSession.contacts.toString()
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editAbstractSession.stickingPoints
        }
    }
}

@Composable
fun timeInputButton(
    text: String, dialogState: MaterialDialogState
) {
    Button(onClick = { dialogState.show() }) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun formSectionDescription(
    text: String, descriptionFontSize: TextUnit
) {
    Text(
        text = text,
        fontSize = descriptionFontSize,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Center
    )
}
