package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.PhoneBookService
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import kotlinx.coroutines.delay

@Composable
fun LeadDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
        .height(470.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
    val textModifier = if (state.isUpdatingLead) {
        Modifier
            .height(60.dp)
            .width(200.dp)
    } else {
        Modifier
            .height(60.dp)
            .fillMaxWidth()
    }
    AlertDialog(
        modifier = modifier
            .shadow(elevation = 10.dp), onDismissRequest = {
            onEvent(GameEvent.SwitchSaveLeadToLiveSession)
            onEvent(GameEvent.SetIsInOverlayToFalse)
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
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        readOnly = state.isModifyingLead,
                        value = state.leadName,
                        onValueChange = { onEvent(GameEvent.SetLeadName(it)) },
                        placeholder = { LittleBodyText("Insert lead name") },
                        shape = MaterialTheme.shapes.large,
                        modifier = textModifier
                    )
                    if (state.isUpdatingLead) {
                        Spacer(modifier = Modifier.width(10.dp))
                        IconShadowButton(
                            onClick = {
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
                                onEvent(GameEvent.SetIsInOverlayToFalse)
                                onEvent(
                                    GameEvent.HideLeadDialog
                                )
                                Toast.makeText(context, "Lead deleted", Toast.LENGTH_SHORT).show()
                            },
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Lead"
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LaunchedEffect(state.countrySearch) {
                        if (state.countrySearch.isNotEmpty()) {
                            delay(500L)
                            expanded = true
                        }
                    }
                    OutlinedTextField(
                        readOnly = state.isModifyingLead,
                        value = if (state.countrySearch.isEmpty()) CountryEnum.getFlagByAlpha3(
                            state.leadNationality
                        ) + " " + CountryEnum.getCountryNameByAlpha3(
                            state.leadNationality
                        ) else state.countrySearch,
                        onValueChange = {
                            onEvent(GameEvent.SetLeadCountrySearch(it))
                        },
                        placeholder = { LittleBodyText("Search lead country") },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .height(60.dp)
                            .width(200.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconShadowButton(
                        onClick = {
                            onEvent(GameEvent.SetLeadCountrySearch(""))
                            expanded = true
                        },
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Select country"
                    )
                }
                DropdownMenu(
                    modifier = Modifier
                        .width(200.dp)
                        .height(450.dp),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    var count = 0
                    CountryEnum.getInsertCountries(
                        state.mostPopularLeadsNationalities,
                        state.suggestLeadsNationality,
                        state.countrySearch
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
                                    onEvent(GameEvent.SetLeadCountrySearch(""))
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
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(0.65f)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = "", textAlign = TextAlign.Center)
                            ToggleIcon(
                                "",
                                ContactTypeEnum.NUMBER.getField().equals(state.leadContact),
                                false,
                                if (isDarkTheme) R.drawable.whatsapp_w else R.drawable.whatsapp_b,
                                !state.leadContactLookupKey.isNullOrBlank() && state.leadContact == ContactTypeEnum.NUMBER.getField()
                            ) {
                                onEvent(GameEvent.SetLeadContact(ContactTypeEnum.NUMBER.getField()))
                                val contactInfo = PhoneBookService.findSimilarContact(
                                    localContext.contentResolver,
                                    state.leadName
                                )
                                if (contactInfo != null) {
                                    Toast.makeText(
                                        context,
                                        "Contact found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onEvent(GameEvent.SetLeadContactLookupKey(contactInfo!!.second))
                                }
                            }
                            ToggleIcon(
                                "",
                                ContactTypeEnum.SOCIAL.getField().equals(state.leadContact),
                                false,
                                if (isDarkTheme) R.drawable.instagram_w else R.drawable.instagram_b,
                                !state.leadInstagramUrl.isNullOrBlank() && state.leadContact == ContactTypeEnum.SOCIAL.getField()
                            ) {
                                // TODO: put this logic in a centralized service:
                                val textFromClipboard = clipboardManager.getText()
                                if (textFromClipboard != null) {
                                    var instagramUrl: String =
                                        textFromClipboard!!.toString()
                                    if (instagramUrl.startsWith("https://www.instagram.com/")) {
                                        onEvent(GameEvent.SetLeadInstagramUrl(instagramUrl))
                                        Toast.makeText(
                                            localContext,
                                            "Copied profile url",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                onEvent(GameEvent.SetLeadContact(ContactTypeEnum.SOCIAL.getField()))
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            LittleBodyText("Whatsapp will trigger a name-based phonebook contact search.\nInstagram copies profile url from clipboard.")
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(60.dp)
                    ) {
                        InputCountComponent(
                            inputTitle = "Years old",
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
                if (state.isUpdatingLead || state.saveLeadToLiveSession) {
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
                lead.contactLookupKey = state.leadContactLookupKey
                lead.instagramUrl = state.leadInstagramUrl
                if (state.isModifyingLead) {
                    onEvent(
                        GameEvent.DeleteLead(
                            lead
                        )
                    )
                }
                if (state.isUpdatingLead || state.saveLeadToLiveSession) {
                    onEvent(GameEvent.SaveLead(lead))
                } else {
                    onEvent(GameEvent.SetLead(lead))
                }
                onEvent(GameEvent.SwitchSaveLeadToLiveSession)
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideLeadDialog)
                onEvent(GameEvent.SwitchJustSaved)
                if (state.isUpdatingLead || state.saveLeadToLiveSession) {
                    Toast.makeText(context, "Lead saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Lead on hold", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.SwitchSaveLeadToLiveSession)
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideLeadDialog)
            }
        })
}