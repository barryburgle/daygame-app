package com.barryburgle.gameapp.ui.date

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.date.state.DateState
import com.barryburgle.gameapp.ui.input.InputCounter
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    state: DateState,
    onEvent: (DateEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    val dateDialogState = rememberMaterialDialogState()
    val startHourDialogState = rememberMaterialDialogState()
    val endHourDialogState = rememberMaterialDialogState()
    var expanded by remember { mutableStateOf(false) }
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
                        if (state.leadId == 0L) {
                            Column(
                                modifier = Modifier.width(sessionLeadColumnWidth - addLeadColumnWidth)
                            ) {
                                com.barryburgle.gameapp.ui.input.formSectionDescription(
                                    "Add lead:",
                                    descriptionFontSize
                                )
                            }
                            Column(
                                modifier = Modifier.width(addLeadColumnWidth)
                            ) {
                                IconButton(onClick = {
                                    expanded = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
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
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                state.allLeads.forEach { lead ->
                                    DropdownMenuItem(
                                        text = { Text(text = CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age) },
                                        onClick = {
                                            onEvent(DateEvent.SetLeadId(lead.id))
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        } else {
                            // TODO: display a button with name flag and year of the lead that is clickable to evoke the dropdown menu
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        toggleIcon(
                            "pull",
                            state.pull,
                            R.drawable.pull_b
                        ) {
                            onEvent(DateEvent.SwitchPull)
                        }
                        toggleIcon(
                            "bounce",
                            state.bounce,
                            R.drawable.bounce_b
                        ) {
                            onEvent(DateEvent.SwitchBounce)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        toggleIcon(
                            "kiss",
                            state.kiss,
                            R.drawable.kiss_b
                        ) {
                            onEvent(DateEvent.SwitchKiss)
                        }
                        toggleIcon(
                            "lay",
                            state.lay,
                            R.drawable.bed_b
                        ) {
                            onEvent(DateEvent.SwitchLay)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        toggleIcon(
                            "record",
                            state.recorded,
                            R.drawable.microphone_b
                        ) {
                            onEvent(DateEvent.SwitchRecorded)
                        }
                        //TODO: set the following to copy and import the following the tweet link (validating it, verifying it starts by https...)
                        toggleIcon(
                            "lay",
                            state.lay,
                            R.drawable.bed_b
                        ) {
                            onEvent(DateEvent.SwitchLay)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
            OutlinedTextField(
                value = state.stickingPoints,
                onValueChange = { onEvent(DateEvent.SetStickingPoints(it)) },
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
        state.date = state.editDate.date!!.substring(0, 10)
        state.startHour = state.editDate.startHour.substring(11, 16)
        state.endHour = state.editDate.endHour.substring(11, 16)
        state.dateNumber = state.editDate.dateNumber.toString()
        state.cost = state.editDate.cost.toString()
        state.pull = state.editDate.pull
        state.bounce = state.editDate.bounce
        state.kiss = state.editDate.kiss
        state.lay = state.editDate.lay
        state.recorded = state.editDate.recorded
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editDate.stickingPoints!!
        }
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

@Composable
fun toggleIcon(
    description: String,
    flag: Boolean,
    @DrawableRes icon: Int,
    onCheckedChange: () -> Unit
) {
    // TODO: refactor method in a separate file
    IconButton(
        onClick = {
            onCheckedChange()
        },
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .size(40.dp)
    ) {
        var color: Color = MaterialTheme.colorScheme.onPrimary
        if (!flag) {
            color = MaterialTheme.colorScheme.primaryContainer
        }
        Image(
            painter = painterResource(icon),
            contentDescription = description,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp),
            colorFilter = ColorFilter.tint(color)
        )
    }
}