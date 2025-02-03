package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.state.InputState

@Composable
fun AddLeadDialog(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
        .height(460.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
    val numberFlag = state.leadContact == ContactTypeEnum.NUMBER.getField()
    val socialFlag = state.leadContact == ContactTypeEnum.SOCIAL.getField()
    AlertDialog(modifier = modifier
        .shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(AbstractSessionEvent.HideLeadDialog)
    }, title = {
        Text(text = description)
    }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textModifier = if (state.isUpdatingLead) {
                    Modifier
                        .height(60.dp)
                        .width(200.dp)
                } else {
                    Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                }
                OutlinedTextField(
                    readOnly = state.isModifyingLead,
                    value = state.leadName,
                    onValueChange = { onEvent(AbstractSessionEvent.SetLeadName(it)) },
                    placeholder = { Text(text = "Name") },
                    shape = MaterialTheme.shapes.large,
                    modifier = textModifier
                )
                if (state.isUpdatingLead) {
                    IconButton(onClick = {
                        lead.id = state.leadId
                        lead.name = state.leadName
                        lead.contact = state.leadContact
                        lead.nationality = state.leadNationality
                        lead.age = state.leadAge
                        onEvent(
                            AbstractSessionEvent.DeleteLead(
                                lead
                            )
                        )
                        onEvent(
                            AbstractSessionEvent.HideLeadDialog
                        )
                        Toast.makeText(localContext, "Lead deleted", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Lead",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { expanded = true },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.leadNationality.isBlank()) "Touch to choose a country" else CountryEnum.getFlagByAlpha3(
                        state.leadNationality
                    ) + "   " + CountryEnum.getCountryNameByAlpha3(
                        state.leadNationality
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (!state.leadNationality.isBlank()) {
                    Text(text = CountryEnum.getFlagByAlpha3(state.leadNationality))
                }
            }
            DropdownMenu(
                modifier = Modifier
                    .width(200.dp)
                    .height(450.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CountryEnum.getCountriesOrderedByName().forEach { country ->
                    DropdownMenuItem(
                        text = { Text(text = country.flag + "  " + country.countryName) },
                        onClick = {
                            onEvent(AbstractSessionEvent.SetLeadNationality(country.alpha3))
                            expanded = false
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Number"
                        )
                        getSwitch(numberFlag, onEvent, ContactTypeEnum.NUMBER)
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Social"
                        )
                        getSwitch(socialFlag, onEvent, ContactTypeEnum.SOCIAL)
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(60.dp)
                ) {
                    InputCountComponent(
                        inputTitle = "Age",
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        onEvent = onEvent,
                        countStart = state.leadAge.toInt(),
                        saveEvent = AbstractSessionEvent::SetLeadAge
                    )
                }
            }
        }
    }, confirmButton = {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .width(250.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onEvent(AbstractSessionEvent.HideLeadDialog)
                    }
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (state.isUpdatingLead) {
                            lead.id = state.leadId
                            lead.insertTime = state.leadInsertTime
                            lead.sessionId = state.leadSessionId
                        }
                        if (!state.isModifyingLead) {
                            lead.name = state.leadName
                        }
                        lead.contact = state.leadContact
                        lead.nationality = state.leadNationality
                        lead.age = state.leadAge
                        if (state.isModifyingLead) {
                            onEvent(
                                AbstractSessionEvent.DeleteLead(
                                    lead
                                )
                            )
                        }
                        if (state.isUpdatingLead) {
                            onEvent(AbstractSessionEvent.SaveLead(lead))
                        } else {
                            onEvent(AbstractSessionEvent.SetLead(lead))
                        }
                        onEvent(AbstractSessionEvent.HideLeadDialog)
                        if (state.isUpdatingLead) {
                            Toast.makeText(localContext, "Lead saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(localContext, "Lead on hold", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    })
}

@Composable
fun getSwitch(
    flag: Boolean,
    onEvent: (AbstractSessionEvent) -> Unit,
    contactTypeEnum: ContactTypeEnum
) {
    Switch(
        checked = flag,
        onCheckedChange = {
            onEvent(AbstractSessionEvent.SetLeadContact(contactTypeEnum.getField()))
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = getThumbColor(flag),
            checkedTrackColor = getTrackColor(flag),
            uncheckedThumbColor = getThumbColor(flag),
            uncheckedTrackColor = getTrackColor(flag)
        )
    )
}

// TODO: unify all switches colors (those and in tools card) color maangement
@Composable
fun getThumbColor(flag: Boolean): Color {
    val thumbColor by animateColorAsState(
        targetValue = if (flag) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 500)
    )
    return thumbColor
}

@Composable
fun getTrackColor(flag: Boolean): Color {
    val trackColor by animateColorAsState(
        targetValue = if (flag) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 500)
    )
    return trackColor
}