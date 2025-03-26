package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.ToggleIcon

@Composable
fun LeadDialog(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
        .height(460.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
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
                val isDarkTheme = isSystemInDarkTheme()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(text = "", textAlign = TextAlign.Center)
                    ToggleIcon(
                        "Whatsapp",
                        ContactTypeEnum.NUMBER.getField().equals(state.leadContact),
                        if (isDarkTheme) R.drawable.whatsapp_w else R.drawable.whatsapp_b
                    ) { onEvent(AbstractSessionEvent.SetLeadContact(ContactTypeEnum.NUMBER.getField())) }
                    ToggleIcon(
                        "Instagram",
                        ContactTypeEnum.SOCIAL.getField().equals(state.leadContact),
                        if (isDarkTheme) R.drawable.instagram_w else R.drawable.instagram_b
                    ) { onEvent(AbstractSessionEvent.SetLeadContact(ContactTypeEnum.SOCIAL.getField())) }
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
                        onEvent = onEvent as (GenericEvent) -> Unit,
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
                        onEvent(AbstractSessionEvent.SwitchJustSaved)
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