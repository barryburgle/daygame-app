package com.barryburgle.gameapp.ui.input.dialog

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
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.model.enums.TimeInputFormEnum
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkImportButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.TimeInputFormButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SetDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    val setDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
    if (state.isUpdatingSet) {
        setState(state)
    }
    // TODO: re-use common parts in an inheritable event dialog
    MaterialDialog(
        dialogState = setDialogState, elevation = 10.dp, buttons = {
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
            initialDate = if (state.isAddingSet || state.editSet == null) LocalDate.now() else FormatService.parseDate(
                state.editSet.date
            ), title = "Set date", colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.onTertiary,
                dateInactiveTextColor = MaterialTheme.colorScheme.background
            )
        ) {
            onEvent(GameEvent.SetDate(it.toString()))
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
            initialTime = if (state.isAddingSet || state.editSet == null) LocalTime.now() else FormatService.parseTime(
                state.editSet!!.startHour
            ), title = "Set start hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(GameEvent.SetStartHour(it.toString().substring(0, 5)))
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
            initialTime = if (state.isAddingSet || state.editSet == null) LocalTime.now() else FormatService.parseTime(
                state.editSet!!.endHour
            ), title = "Set end hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(GameEvent.SetEndHour(it.toString().substring(0, 5)))
        }
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.HideDialog)
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
                    .height(DialogConstant.ADD_LEAD_COLUMN_WIDTH),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.width(DialogConstant.TIME_COLUMN_WIDTH)
                        ) {
                            DialogFormSectionDescription(
                                "Set set's:",
                                DialogConstant.DESCRIPTION_FONT_SIZE
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        var leadIcon = Icons.Default.Add
                        var editLead: Boolean = false
                        if (!state.isUpdatingSet) {
                            Column(
                                modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                            ) {
                                if (state.leads.isEmpty()) {
                                    DialogFormSectionDescription(
                                        "Add lead:",
                                        DialogConstant.DESCRIPTION_FONT_SIZE
                                    )
                                } else {
                                    val lead = state.leads.get(0)
                                    DialogFormSectionDescription(
                                        CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age,
                                        DialogConstant.DESCRIPTION_FONT_SIZE
                                    )
                                    leadIcon = Icons.Default.SwapHoriz
                                    editLead = true
                                }
                            }
                            Column(
                                modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                            ) {
                                IconButton(onClick = {
                                    if (editLead) {
                                        onEvent(GameEvent.EmptyLeads)
                                    }
                                    onEvent(GameEvent.ShowLeadDialog(true, false))
                                }) {
                                    Icon(
                                        imageVector = leadIcon,
                                        contentDescription = "Add a lead",
                                        tint = MaterialTheme.colorScheme.inversePrimary,
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.secondaryContainer
                                            )
                                            .width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                                            .height(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    TimeInputFormButton(
                        state.date,
                        latestDateValue,
                        TimeInputFormEnum.DATE,
                        state.isAddingSet,
                        state.editSet,
                        if (state.editSet != null) state.editSet!!.date else "",
                        "session",
                        ""
                    ) {
                        onEvent(GameEvent.SetDate(it))
                    }
                    TimeInputFormButton(
                        state.startHour,
                        latestStartHour,
                        TimeInputFormEnum.HOUR,
                        state.isAddingSet,
                        state.editSet,
                        if (state.editSet != null) state.editSet!!.startHour else "",
                        "session",
                        "Start"
                    ) {
                        onEvent(GameEvent.SetStartHour(it.substring(0, 5)))
                    }
                    TimeInputFormButton(
                        state.endHour,
                        latestEndHour,
                        TimeInputFormEnum.HOUR,
                        state.isAddingSet,
                        state.editSet,
                        if (state.editSet != null) state.editSet!!.endHour else "",
                        "session",
                        "End"
                    ) {
                        onEvent(GameEvent.SetEndHour(it.substring(0, 5)))
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = { locationTextFieldExpanded = !locationTextFieldExpanded }) {
                        // TODO: insert here a button that allows to choose from a list of sessions to which the set belongs to
                        // TODO: add a switch that allow to automatically create a date from a set, and a selector in case the idate has
                        // already been recorded on table
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = state.dateType,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .height(15.dp)
                            )
                            Text(
                                text = "Location",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    TweetLinkImportButton(onEvent)
                }
            }
            BasicAnimatedVisibility(
                visibilityFlag = locationTextFieldExpanded,
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = state.location,
                    onValueChange = { onEvent(GameEvent.SetLocation(it)) },
                    placeholder = { Text(text = "Location") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
            }
            BasicAnimatedVisibility(
                visibilityFlag = !locationTextFieldExpanded,
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = state.stickingPoints,
                    onValueChange = { onEvent(GameEvent.SetStickingPoints(it)) },
                    placeholder = { Text(text = "Sticking Points") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ToggleIcon(
                    SetSortType.CONVERSATION.getField(),
                    state.conversation,
                    true,
                    R.drawable.chat_b
                ) {
                    onEvent(GameEvent.SwitchConversation)
                }
                ToggleIcon(
                    "contact",
                    state.contact,
                    true,
                    R.drawable.contact_b
                ) {
                    onEvent(GameEvent.SwitchContact)
                }
                ToggleIcon(
                    "instant\ndate",
                    state.instantDate,
                    true,
                    R.drawable.idate_b
                ) {
                    onEvent(GameEvent.SwitchInstantDate)
                }
                ToggleIcon(
                    "recorded",
                    state.recorded,
                    true,
                    R.drawable.microphone_b
                ) {
                    onEvent(GameEvent.SwitchRecorded)
                }
            }
        }
    }, confirmButton = {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.width(250.dp), horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    onEvent(GameEvent.HideDialog)
                }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    onEvent(GameEvent.SaveSet)
                    onEvent(GameEvent.HideDialog)
                    onEvent(GameEvent.SwitchJustSaved)
                    Toast.makeText(localContext, "Set saved", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Save")
                }
            }
        }
    })
}

private fun setState(
    state: InputState
) {
    if (state.editSet != null) {
        state.date = state.editSet.date.substring(0, 10)
        state.startHour = state.editSet.startHour.substring(11, 16)
        state.endHour = state.editSet.endHour.substring(11, 16)
        // TODO: solve following
        // state.sessionId = state.editSet.sessionId!!
        if (state.location.isBlank()) {
            state.location = state.editSet.location!!
        }
        state.conversation = state.editSet.conversation
        state.contact = state.editSet.contact
        state.instantDate = state.editSet.instantDate
        state.recorded = state.editSet.recorded
        state.leadId = state.editSet.leadId!!
        // TODO: solve following
        // state.dateId = state.editSet.dateId!!
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editSet.stickingPoints!!
        }
        state.tweetUrl = state.editSet.tweetUrl!!
    }
}
