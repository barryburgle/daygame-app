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
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.SetSortType
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
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
    if (state.isUpdatingSet) {
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
                                IconShadowButton(
                                    onClick = {
                                        if (editLead) {
                                            onEvent(GameEvent.EmptyLeads)
                                        }
                                        onEvent(GameEvent.ShowLeadDialog(true, false))
                                    },
                                    imageVector = leadIcon,
                                    contentDescription = "Add a lead"
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
                Column {
                    // TODO: insert here a button that allows to choose from a list of sessions to which the set belongs to  [v1.10.0]
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
                horizontalArrangement = Arrangement.SpaceBetween
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
                    if (!state.instantDate && state.generateiDate) {
                        Toast.makeText(
                            localContext,
                            "Generating related iDate",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
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
            onEvent(GameEvent.SaveSet)
            onEvent(GameEvent.HideDialog)
            onEvent(GameEvent.SwitchJustSaved)
            Toast.makeText(localContext, "Set saved", Toast.LENGTH_SHORT).show()
        }
    },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.HideDialog)
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
        // TODO: solve following [v1.7.3]
        // state.sessionId = state.editSet.sessionId!!
        if (state.location.isBlank()) {
            state.location = state.editSet.location!!
        }
        state.leadId = state.editSet.leadId!!
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editSet.stickingPoints!!
        }
        state.tweetUrl = state.editSet.tweetUrl!!
    }
}
