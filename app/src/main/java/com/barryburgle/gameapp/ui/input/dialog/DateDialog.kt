package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.DialogTimeFormSection
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun DateDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    var leadsExpanded by remember { mutableStateOf(false) }
    var dateTypesExpanded by remember { mutableStateOf(false) }
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
    if (state.isUpdatingDate) {
        setState(state)
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.HideDialog)
    }, title = {
        LargeTitleText(text = description)
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
                                val foundLead =
                                    state.allLeads.filter { lead -> lead.id == state.leadId }
                                if (foundLead.size != 0) {
                                    val lead = foundLead.get(0)
                                    DialogFormSectionDescription(
                                        CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age,
                                        DialogConstant.DESCRIPTION_FONT_SIZE
                                    )
                                    leadIcon = Icons.Default.SwapHoriz
                                }
                            }
                        }
                        Column(
                            modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                        ) {
                            IconShadowButton(
                                onClick = {
                                    leadsExpanded = true
                                },
                                imageVector = leadIcon,
                                contentDescription = "Add lead"
                            )
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
                                    text = { LittleBodyText(CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age) },
                                    onClick = {
                                        onEvent(GameEvent.SetLeadId(lead.id))
                                        leadsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DialogTimeFormSection(state, onEvent, latestDateValue, latestStartHour, latestEndHour)
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    // TODO: align the buttons in the column in the right with column on the left
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
                                        LittleBodyText(dateType.getType()
                                            .replaceFirstChar { it.uppercase() })
                                    }
                                },
                                onClick = {
                                    onEvent(GameEvent.SetDateType(dateType.getType()))
                                    dateTypesExpanded = false
                                }
                            )
                        }
                    }
                    IconShadowButton(
                        onClick = {
                            dateTypesExpanded = true
                        },
                        imageVector = DateType.getIcon(state.dateType),
                        contentDescription = "Date type",
                        title = if (state.dateType.isBlank()) "Date type" else state.dateType.replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                    )
                    IconShadowButton(
                        onClick = {
                            locationTextFieldExpanded = !locationTextFieldExpanded
                        },
                        imageVector = Icons.Default.PinDrop,
                        contentDescription = "Location",
                        title = "Location",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                    )
                    val clipboardManager: ClipboardManager = LocalClipboardManager.current
                    val localContext = LocalContext.current.applicationContext
                    IconShadowButton(
                        onClick = {
                            var tweetUrl: String = clipboardManager.getText()!!.toString()
                            if (tweetUrl.startsWith("https://x.com/")) {
                                onEvent(GameEvent.SetTweetUrl(tweetUrl))
                                Toast.makeText(
                                    localContext,
                                    "Copied tweet url",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "Tweet Url",
                        title = "Tweet Url",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                    )
                }
            }
            BasicAnimatedVisibility(
                visibilityFlag = locationTextFieldExpanded,
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = state.location,
                    onValueChange = { onEvent(GameEvent.SetLocation(it)) },
                    placeholder = { LittleBodyText("Location") },
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
                    placeholder = { LittleBodyText("Sticking Points") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InputCountComponent(
                    inputTitle = "Date Number",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingDate) 0 else state.editDate?.dateNumber,
                    saveEvent = GameEvent::SetDateNumber
                )
                InputCountComponent(
                    inputTitle = "Cost [€]",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingDate) 0 else state.editDate?.cost,
                    saveEvent = GameEvent::SetCost
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ToggleIcon(
                    "pull",
                    state.pull,
                    false,
                    R.drawable.pull_b
                ) {
                    onEvent(GameEvent.SwitchPull)
                }
                ToggleIcon(
                    "bounce",
                    state.bounce,
                    false,
                    R.drawable.bounce_b
                ) {
                    onEvent(GameEvent.SwitchBounce)
                }
                ToggleIcon(
                    "kiss",
                    state.kiss,
                    false,
                    R.drawable.kiss_b
                ) {
                    onEvent(GameEvent.SwitchKiss)
                }
                ToggleIcon(
                    "lay",
                    state.lay,
                    false,
                    R.drawable.bed_b
                ) {
                    onEvent(GameEvent.SwitchLay)
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
        ConfirmButton {
            if (state.leadId == 0L) {
                Toast.makeText(localContext, "Please choose a lead", Toast.LENGTH_SHORT)
                    .show()
            } else {
                onEvent(GameEvent.SaveDate)
                onEvent(GameEvent.HideDialog)
                onEvent(GameEvent.SwitchJustSaved)
                Toast.makeText(localContext, "Date saved", Toast.LENGTH_SHORT).show()
            }
        }
    },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.HideDialog)
            }
        }
    )
}

private fun setState(
    state: InputState
) {
    if (state.editDate != null) {
        if (state.leadId == 0L) {
            state.leadId = state.editDate.leadId!!
        }
        if (state.location.isBlank()) {
            state.location = state.editDate.location!!
        }
        state.date = state.editDate.date!!.substring(0, 10)
        state.startHour = state.editDate.startHour.substring(11, 16)
        state.endHour = state.editDate.endHour.substring(11, 16)
        state.cost = state.editDate.cost.toString()
        state.dateNumber = state.editDate.dateNumber.toString()
        if (state.dateType.isBlank()) {
            state.dateType = state.editDate.dateType
        }
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editDate.stickingPoints!!
        }
        state.tweetUrl = state.editDate.tweetUrl!!
    }
}