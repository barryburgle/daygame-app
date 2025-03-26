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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.date.state.DateState
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val dateDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    var leadsExpanded by remember { mutableStateOf(false) }
    var dateTypesExpanded by remember { mutableStateOf(false) }
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
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
                                "Set date's:",
                                DialogConstant.DESCRIPTION_FONT_SIZE
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        var leadIcon = Icons.Default.Add
                        Column(
                            modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                        ) {
                            if (state.leadId == 0L) {
                                DialogFormSectionDescription(
                                    "Add lead:",
                                    DialogConstant.DESCRIPTION_FONT_SIZE
                                )
                            } else {
                                val lead =
                                    state.allLeads.filter { lead -> lead.id == state.leadId }.get(0)
                                DialogFormSectionDescription(
                                    CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age,
                                    DialogConstant.DESCRIPTION_FONT_SIZE
                                )
                                leadIcon = Icons.Default.SwapHoriz
                            }
                        }
                        Column(
                            modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
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
                                        .width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                                        .height(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
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
                    ), onClick = {
                        var tweetUrl: String = clipboardManager.getText()!!.toString()
                        if (tweetUrl.startsWith("https://x.com/")) {
                            onEvent(DateEvent.SetTweetUrl(tweetUrl))
                            Toast.makeText(
                                localContext,
                                "Copied tweet url",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
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
                InputCountComponent(
                    inputTitle = "Date Number",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingDate) 0 else state.editDate?.dateNumber,
                    saveEvent = DateEvent::SetDateNumber
                )
                InputCountComponent(
                    inputTitle = "Cost [€]",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
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
                    if (state.leadId == 0L) {
                        Toast.makeText(localContext, "Please choose a lead", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        onEvent(DateEvent.SaveDate)
                        onEvent(DateEvent.HideDialog)
                        Toast.makeText(localContext, "Date saved", Toast.LENGTH_SHORT).show()
                        if (state.backupActive) {
                            runBlocking {
                                async {
                                    DataExchangeService.backup(state)
                                }
                            }
                        }
                    }
                }) {
                    Text(text = "Save")
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
        if (state.location.isBlank()) {
            state.location = state.editDate.location!!
        }
        state.date = state.editDate.date!!.substring(0, 10)
        state.startHour = state.editDate.startHour.substring(11, 16)
        state.endHour = state.editDate.endHour.substring(11, 16)
        state.cost = state.editDate.cost.toString()
        state.dateNumber = state.editDate.dateNumber.toString()
        state.dateType = state.editDate.dateType
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