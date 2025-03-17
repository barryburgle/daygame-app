package com.barryburgle.gameapp.ui.date

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
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.date.state.DateState
import com.barryburgle.gameapp.ui.input.InputCounter
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
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
fun DateDialog(
    state: DateState,
    onEvent: (DateEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    // TODO: make all the fields displayed in the dialog change when in edit mode (similarly to counters)
    val localContext = LocalContext.current.applicationContext
    val dateDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    var leadsExpanded by remember { mutableStateOf(false) }
    var dateTypesExpanded by remember { mutableStateOf(false) }
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
    // TODO: unify across all dialogs
    val descriptionFontSize = 13.sp
    val sessionTimeColumnWidth = 130.dp
    val sessionLeadColumnWidth = 130.dp
    val addLeadColumnWidth = 40.dp
    if (state.isUpdatingDate) {
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
            initialDate = if (state.isAddingDate || state.editDate == null) LocalDate.now() else FormatService.parseDate(
                state.editDate.date!!
            ), title = "Set date", colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.onTertiary,
                dateInactiveTextColor = MaterialTheme.colorScheme.background
            )
        ) {
            onEvent(DateEvent.SetMeetingDate(it.toString()))
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
            initialTime = if (state.isAddingDate || state.editDate == null) LocalTime.now() else FormatService.parseTime(
                state.editDate.startHour!!
            ), title = "Set start hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(DateEvent.SetStartHour(it.toString().substring(0, 5)))
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
            initialTime = if (state.isAddingDate || state.editDate == null) LocalTime.now() else FormatService.parseTime(
                state.editDate.endHour!!
            ), title = "Set end hour", colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(DateEvent.SetEndHour(it.toString().substring(0, 5)))
        }
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(DateEvent.HideDialog)
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
                            formSectionDescription("Set date's:", descriptionFontSize)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        var leadIcon = Icons.Default.Add
                        Column(
                            modifier = Modifier.width(sessionLeadColumnWidth - addLeadColumnWidth)
                        ) {
                            if (state.leadId == 0L) {
                                com.barryburgle.gameapp.ui.input.formSectionDescription(
                                    "Add lead:",
                                    descriptionFontSize
                                )
                            } else {
                                val lead =
                                    state.allLeads.filter { lead -> lead.id == state.leadId }.get(0)
                                com.barryburgle.gameapp.ui.input.formSectionDescription(
                                    CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age,
                                    descriptionFontSize
                                )
                                leadIcon = Icons.Default.SwapHoriz
                            }
                        }
                        Column(
                            modifier = Modifier.width(addLeadColumnWidth)
                        ) {
                            IconButton(onClick = {
                                leadsExpanded = true
                            }) {
                                Icon(
                                    imageVector = leadIcon,
                                    contentDescription = "Add lead",
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
                        DropdownMenu(
                            modifier = Modifier
                                .width(200.dp)
                                .height(450.dp),
                            expanded = leadsExpanded,
                            onDismissRequest = { leadsExpanded = false }
                        ) {
                            state.allLeads.forEach { lead ->
                                DropdownMenuItem(
                                    text = { Text(text = CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age) },
                                    onClick = {
                                        onEvent(DateEvent.SetLeadId(lead.id))
                                        leadsExpanded = false
                                    }
                                )
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
                    timeInputButton(
                        getButtonTitle(
                            state.date, "", "Date"
                        ), dateDialogState
                    )
                    timeInputButton(
                        getButtonTitle(state.startHour, "Start ", "Start Hour"),
                        startHourDialogState
                    )
                    timeInputButton(
                        getButtonTitle(state.endHour, "End ", "End Hour"), endHourDialogState
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = { dateTypesExpanded = true }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!state.dateType.isBlank()) {
                                Icon(
                                    imageVector = DateType.getIcon(state.dateType),
                                    contentDescription = state.dateType,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .height(15.dp)
                                )
                            }
                            Text(
                                text = if (state.dateType.isBlank()) "Date type" else state.dateType.replaceFirstChar { it.uppercase() },
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .width(175.dp)
                            .height(280.dp),
                        expanded = dateTypesExpanded,
                        onDismissRequest = { dateTypesExpanded = false }
                    ) {
                        DateType.values().forEach { dateType ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = DateType.getIcon(dateType.getType()),
                                            contentDescription = state.dateType,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .height(15.dp)
                                        )
                                        Text(
                                            text = dateType.getType()
                                                .replaceFirstChar { it.uppercase() })
                                    }
                                },
                                onClick = {
                                    onEvent(DateEvent.SetDateType(dateType.getType()))
                                    dateTypesExpanded = false
                                }
                            )
                        }
                    }
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = { locationTextFieldExpanded = !locationTextFieldExpanded }) {
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
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = { /*TODO: copy the url in clipboard*/ }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = state.dateType,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .height(15.dp)
                            )
                            Text(
                                text = "Tweet Url",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            BasicAnimatedVisibility(
                visibilityFlag = locationTextFieldExpanded,
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = state.location,
                    onValueChange = { onEvent(DateEvent.SetLocation(it)) },
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
                    onValueChange = { onEvent(DateEvent.SetStickingPoints(it)) },
                    placeholder = { Text(text = "Sticking Points") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                InputDateCountComponent(
                    inputTitle = "Date Number",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent,
                    countStart = if (state.isAddingDate) 0 else state.editDate?.dateNumber,
                    saveEvent = DateEvent::SetDateNumber
                )
                InputDateCountComponent(
                    inputTitle = "Cost [€]",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent,
                    countStart = if (state.isAddingDate) 0 else state.editDate?.cost,
                    saveEvent = DateEvent::SetCost
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ToggleIcon(
                    "pull",
                    state.pull,
                    R.drawable.pull_b
                ) {
                    onEvent(DateEvent.SwitchPull)
                }
                ToggleIcon(
                    "bounce",
                    state.bounce,
                    R.drawable.bounce_b
                ) {
                    onEvent(DateEvent.SwitchBounce)
                }
                ToggleIcon(
                    "kiss",
                    state.kiss,
                    R.drawable.kiss_b
                ) {
                    onEvent(DateEvent.SwitchKiss)
                }
                ToggleIcon(
                    "lay",
                    state.lay,
                    R.drawable.bed_b
                ) {
                    onEvent(DateEvent.SwitchLay)
                }
                ToggleIcon(
                    "record",
                    state.recorded,
                    R.drawable.microphone_b
                ) {
                    onEvent(DateEvent.SwitchRecorded)
                }
                //TODO: set the following to copy and import the following the tweet link (validating it, verifying it starts by https...)
                /*ToggleIcon(
                    "lay",
                    state.lay,
                    R.drawable.bed_b
                ) {
                    onEvent(DateEvent.SwitchLay)
                }*/
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
                    onEvent(DateEvent.HideDialog)
                }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    onEvent(DateEvent.SaveDate)
                    onEvent(DateEvent.HideDialog)
                    Toast.makeText(localContext, "Date saved", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Save")
                    // TODO: do not allow date insertion without lead insertion or link
                }
            }
        }
    })
}

fun getButtonTitle(stateString: String, addToState: String, buttonDescription: String) =
    if (stateString == null || stateString.isBlank()) buttonDescription else addToState + stateString

fun setState(
    state: DateState
) {
    if (state.editDate != null) {
        state.leadId = state.editDate.leadId!!
        state.location = state.editDate.location!!
        state.date = state.editDate.date!!.substring(0, 10)
        state.startHour = state.editDate.startHour.substring(11, 16)
        state.endHour = state.editDate.endHour.substring(11, 16)
        state.cost = state.editDate.cost.toString()
        state.dateNumber = state.editDate.dateNumber.toString()
        state.dateType = state.editDate.dateType
        state.pull = state.editDate.pull
        state.bounce = state.editDate.bounce
        state.kiss = state.editDate.kiss
        state.lay = state.editDate.lay
        state.recorded = state.editDate.recorded
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editDate.stickingPoints!!
        }
        state.tweetUrl = state.editDate.tweetUrl!!
    }
}

@Composable
fun timeInputButton(
    text: String, dialogState: MaterialDialogState
) {
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), onClick = { dialogState.show() }) {
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
    // TODO: refactor in components
    Text(
        text = text,
        fontSize = descriptionFontSize,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Center
    )
}

@Composable
fun InputDateCountComponent(
    inputTitle: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onEvent: (DateEvent) -> Unit,
    countStart: Int? = 0,
    saveEvent: (input: String) -> DateEvent
) {
    // TODO: delete this composable from this file and use the centralized one adapting it to wor both with AbstractSessionEvent and DateEvent
    var count by remember {
        mutableStateOf(if (countStart == null) 0 else countStart)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = inputTitle, textAlign = TextAlign.Center)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                count--
                onEvent(saveEvent(count.toString()))
            }
        ) {
            Text(text = "-")
        }
        InputCounter(count = count, style = style, modifier = modifier)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                count++
                onEvent(saveEvent(count.toString()))
            }
        ) {
            Text(text = "+")
        }
    }
}