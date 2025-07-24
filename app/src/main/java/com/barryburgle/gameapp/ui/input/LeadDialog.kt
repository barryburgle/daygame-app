package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
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
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun LeadDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
        .height(470.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
    AlertDialog(modifier = modifier
        .shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.HideLeadDialog)
    }, title = {
        LargeTitleText(description)
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
                    onValueChange = { onEvent(GameEvent.SetLeadName(it)) },
                    placeholder = { LittleBodyText("Name") },
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
                            GameEvent.DeleteLead(
                                lead
                            )
                        )
                        onEvent(
                            GameEvent.HideLeadDialog
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
                LittleBodyText(
                    if (state.leadNationality.isBlank()) "Touch to choose a country" else CountryEnum.getFlagByAlpha3(
                        state.leadNationality
                    ) + " " + CountryEnum.getCountryNameByAlpha3(
                        state.leadNationality
                    )
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .width(200.dp)
                    .height(450.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                var count = 0
                CountryEnum.getCountriesOrderedByName(
                    state.mostPopularLeadsNationalities,
                    state.suggestLeadsNationality
                )
                    .forEach { country ->
                        count++
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LittleBodyText(
                                        country.flag + "  " + country.countryName
                                    )
                                    if (count <= state.shownNationalities && state.suggestLeadsNationality) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Suggested country",
                                            tint = MaterialTheme.colorScheme.inversePrimary,
                                            modifier = Modifier
                                                .height(50.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onEvent(GameEvent.SetLeadNationality(country.alpha3))
                                expanded = false
                            }
                        )
                        if (count == state.shownNationalities && state.suggestLeadsNationality) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.5.dp)
                                    .background(color = MaterialTheme.colorScheme.inversePrimary)
                            ) {}
                        }
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
                        false,
                        if (isDarkTheme) R.drawable.whatsapp_w else R.drawable.whatsapp_b
                    ) { onEvent(GameEvent.SetLeadContact(ContactTypeEnum.NUMBER.getField())) }
                    ToggleIcon(
                        "Instagram",
                        ContactTypeEnum.SOCIAL.getField().equals(state.leadContact),
                        false,
                        if (isDarkTheme) R.drawable.instagram_w else R.drawable.instagram_b
                    ) { onEvent(GameEvent.SetLeadContact(ContactTypeEnum.SOCIAL.getField())) }
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
                        saveEvent = GameEvent::SetLeadAge
                    )
                }
            }
        }
    }, confirmButton = {
        ConfirmButton {
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
                    GameEvent.DeleteLead(
                        lead
                    )
                )
            }
            if (state.isUpdatingLead) {
                onEvent(GameEvent.SaveLead(lead))
            } else {
                onEvent(GameEvent.SetLead(lead))
            }
            onEvent(GameEvent.HideLeadDialog)
            onEvent(GameEvent.SwitchJustSaved)
            if (state.isUpdatingLead) {
                Toast.makeText(localContext, "Lead saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(localContext, "Lead on hold", Toast.LENGTH_SHORT).show()
            }
        }
    },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.HideLeadDialog)
            }
        })
}