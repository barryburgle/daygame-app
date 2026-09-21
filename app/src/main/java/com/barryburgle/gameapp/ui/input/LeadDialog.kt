package com.barryburgle.gameapp.ui.input

import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.service.PhoneBookService
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.input.dialog.text.DialogTextComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dropdown.Dropdown
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import kotlinx.coroutines.delay

@Composable
fun InputLeadDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier.height(500.dp)
) {
    LeadDialogContent(
        isUpdatingLead = state.isUpdatingLead,
        leadId = state.leadId,
        leadInsertTime = state.leadInsertTime,
        leadSessionId = state.leadSessionId,
        leadName = state.leadName,
        leadContact = state.leadContact,
        leadNationality = state.leadNationality,
        leadAge = state.leadAge,
        leadContactLookupKey = state.leadContactLookupKey,
        leadInstagramUrl = state.leadInstagramUrl,
        countrySearch = state.countrySearch,
        mostPopularLeadsNationalities = state.mostPopularLeadsNationalities,
        suggestLeadsNationality = state.suggestLeadsNationality,
        saveLeadToLiveSession = state.saveLeadToLiveSession,
        writeHerAfterReminderEnabled = state.writeHerAfterReminderEnabled,
        writeHerReminderInterval = state.writeHerReminderInterval,
        onSetLeadName = { onEvent(GameEvent.SetLeadName(it)) },
        onSetLeadCountrySearch = { onEvent(GameEvent.SetLeadCountrySearch(it)) },
        onSetLeadNationality = { onEvent(GameEvent.SetLeadNationality(it)) },
        onSetLeadContact = { onEvent(GameEvent.SetLeadContact(it)) },
        onSetLeadContactLookupKey = { onEvent(GameEvent.SetLeadContactLookupKey(it)) },
        onSetLeadInstagramUrl = { onEvent(GameEvent.SetLeadInstagramUrl(it)) },
        onSetLeadAge = { onEvent(GameEvent.SetLeadAge(it)) },
        onSaveLead = { onEvent(GameEvent.SaveLead(it)) },
        onSetLead = { onEvent(GameEvent.SetLead(it)) },
        onDeleteLead = { onEvent(GameEvent.DeleteLead(state.leadId)) },
        onDismiss = {
            if (state.leadSessionId != null) {
                onEvent(GameEvent.RollbackContactPinPointForLeadInsertDismissal(state.leadSessionId))
            }
            onEvent(GameEvent.SwitchSaveLeadToLiveSession)
            onEvent(GameEvent.SetIsInOverlayToFalse)
            onEvent(GameEvent.HideLeadDialog)
        },
        onScheduleReminder = { interval, leadDesc, notificationLink ->
            onEvent(GameEvent.ScheduleWriteHerAfterReminder(interval, leadDesc, notificationLink))
        },
        onPostConfirm = {
            onEvent(GameEvent.SwitchSaveLeadToLiveSession)
            onEvent(GameEvent.SetIsInOverlayToFalse)
            onEvent(GameEvent.HideLeadDialog)
            onEvent(GameEvent.SwitchJustSaved)
        },
        description = description,
        modifier = modifier
    )
}

@Composable
fun OutputLeadDialog(
    state: OutputState,
    onEvent: (OutputEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier.height(500.dp)
) {
    LeadDialogContent(
        isUpdatingLead = state.isUpdatingLead,
        leadId = state.leadId,
        leadInsertTime = state.leadInsertTime,
        leadSessionId = state.leadSessionId,
        leadName = state.leadName,
        leadContact = state.leadContact,
        leadNationality = state.leadNationality,
        leadAge = state.leadAge,
        leadContactLookupKey = state.leadContactLookupKey,
        leadInstagramUrl = state.leadInstagramUrl,
        countrySearch = state.countrySearch,
        mostPopularLeadsNationalities = state.mostPopularLeadsNationalities,
        suggestLeadsNationality = state.suggestLeadsNationality,
        saveLeadToLiveSession = false,
        writeHerAfterReminderEnabled = false,
        writeHerReminderInterval = 60,
        onSetLeadName = { onEvent(OutputEvent.SetLeadName(it)) },
        onSetLeadCountrySearch = { onEvent(OutputEvent.SetLeadCountrySearch(it)) },
        onSetLeadNationality = { onEvent(OutputEvent.SetLeadNationality(it)) },
        onSetLeadContact = { onEvent(OutputEvent.SetLeadContact(it)) },
        onSetLeadContactLookupKey = { onEvent(OutputEvent.SetLeadContactLookupKey(it)) },
        onSetLeadInstagramUrl = { onEvent(OutputEvent.SetLeadInstagramUrl(it)) },
        onSetLeadAge = { onEvent(OutputEvent.SetLeadAge(it)) },
        onSaveLead = { onEvent(OutputEvent.SaveLead(it)) },
        onSetLead = { onEvent(OutputEvent.SaveLead(it)) },
        onDeleteLead = { onEvent(OutputEvent.DeleteLead(state.leadId)) },
        onDismiss = {
            onEvent(OutputEvent.SetIsInOverlayToFalse)
            onEvent(OutputEvent.HideLeadDialog)
        },
        onScheduleReminder = null,
        onPostConfirm = {
            onEvent(OutputEvent.SetIsInOverlayToFalse)
            onEvent(OutputEvent.HideLeadDialog)
        },
        description = description,
        modifier = modifier
    )
}

@Composable
fun LeadDialogContent(
    isUpdatingLead: Boolean,
    leadId: Long,
    leadInsertTime: String,
    leadSessionId: Long?,
    leadName: String,
    leadContact: String,
    leadNationality: String,
    leadAge: Long,
    leadContactLookupKey: String?,
    leadInstagramUrl: String?,
    countrySearch: String,
    mostPopularLeadsNationalities: List<CategoryHistogram>,
    suggestLeadsNationality: Boolean,
    saveLeadToLiveSession: Boolean,
    writeHerAfterReminderEnabled: Boolean,
    writeHerReminderInterval: Int,
    onSetLeadName: (String) -> Unit,
    onSetLeadCountrySearch: (String) -> Unit,
    onSetLeadNationality: (String) -> Unit,
    onSetLeadContact: (String) -> Unit,
    onSetLeadContactLookupKey: (String) -> Unit,
    onSetLeadInstagramUrl: (String) -> Unit,
    onSetLeadAge: (String) -> Unit,
    onSaveLead: (Lead) -> Unit,
    onSetLead: (Lead) -> Unit,
    onDeleteLead: (Long) -> Unit,
    onDismiss: () -> Unit,
    onScheduleReminder: ((interval: Int, leadDesc: String, notificationLink: String) -> Unit)? = null,
    onPostConfirm: () -> Unit,
    description: String,
    modifier: Modifier = Modifier.height(480.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val textModifier = if (isUpdatingLead) {
        Modifier
            .height(60.dp)
            .width(200.dp)
    } else {
        Modifier
            .height(60.dp)
            .fillMaxWidth()
    }
    var showDeleteLeadDialog by remember { mutableStateOf(false) }
    if (showDeleteLeadDialog) {
        DeleteConfirmationDialog(
            "lead ${leadName}",
            "Do you want to delete the lead \"${leadName}\"?",
            onConfirmRequest = {
                onDeleteLead(leadId)
                showDeleteLeadDialog = false
                onDismiss()
            },
            onDismissRequest = {
                showDeleteLeadDialog = false
            },
        )
    }
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.shadow(elevation = 10.dp),
        onDismissRequest = onDismiss,
        title = {
            LargeTitleText(description)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogTextComponent(
                        value = leadName,
                        placeholder = "lead name",
                        singleLine = true
                    ) {
                        onSetLeadName(it)
                    }
                    if (isUpdatingLead) {
                        Spacer(modifier = Modifier.width(10.dp))
                        IconShadowButton(
                            onClick = {
                                showDeleteLeadDialog = true
                            },
                            imageVector = Icons.Default.Delete,
                            iconColor = MaterialTheme.colorScheme.onErrorContainer,
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
                    LaunchedEffect(countrySearch) {
                        if (countrySearch.isNotEmpty()) {
                            delay(500L)
                            expanded = true
                        }
                    }
                    Box {
                        Column(modifier = Modifier.width(200.dp)) {
                            DialogTextComponent(
                                value = if (isFocused) countrySearch else {
                                    if (countrySearch.isEmpty()) CountryEnum.getFlagByAlpha3(
                                        leadNationality
                                    ) + " " + CountryEnum.getCountryNameByAlpha3(
                                        leadNationality
                                    ) else countrySearch
                                },
                                placeholder = "lead country",
                                singleLine = true,
                                onValueChange = {
                                    onSetLeadCountrySearch(it)
                                }
                            )
                        }
                        val countriesList = CountryEnum.getInsertCountries(
                            mostPopularLeadsNationalities,
                            suggestLeadsNationality,
                            countrySearch
                        )
                        var count = 0
                        Dropdown(
                            modifier = Modifier
                                .width(200.dp)
                                .heightIn(max = 450.dp),
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            },
                            items = countriesList,
                            onItemClick = { country ->
                                onSetLeadCountrySearch("")
                                onSetLeadNationality(country.alpha3)
                                isFocused = false
                                focusManager.clearFocus()
                            }
                        ) { country ->
                            count += 1
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.wrapContentSize()
                                    ) {
                                        LargeTitleText(
                                            country.flag
                                        )
                                        if (count <= mostPopularLeadsNationalities.size && suggestLeadsNationality) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Suggested country",
                                                tint = MaterialTheme.colorScheme.onPrimary.copy(
                                                    alpha = 0.85f
                                                ),
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .align(Alignment.TopEnd)
                                                    .offset(x = 6.dp, y = (-6.dp))
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(3.dp))
                                    SmallTitleText(
                                        country.countryName
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconShadowButton(
                        onClick = {
                            onSetLeadCountrySearch("")
                            expanded = true
                        },
                        imageVector = Icons.Default.Flag,
                        secondaryImageVector = Icons.Default.FormatListBulleted,
                        contentDescription = "Select country"
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ToggleIcon(
                                "",
                                ContactTypeEnum.NUMBER.getField().equals(leadContact),
                                if (isDarkTheme) R.drawable.whatsapp_w else R.drawable.whatsapp_b,
                                !leadContactLookupKey.isNullOrBlank() && leadContact == ContactTypeEnum.NUMBER.getField()
                            ) {
                                onSetLeadContact(ContactTypeEnum.NUMBER.getField())
                                val contactInfo = PhoneBookService.findSimilarContact(
                                    localContext.contentResolver, leadName
                                )
                                if (contactInfo != null) {
                                    Toast.makeText(
                                        context,
                                        "Whatsapp contact found and linked from phonebook",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    onSetLeadContactLookupKey(contactInfo.second)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LittleBodyText("Tap on the Whatsapp icon to find and link the contact by name in phonebook. No phone number is stored in this app")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ToggleIcon(
                                "",
                                ContactTypeEnum.SOCIAL.getField().equals(leadContact),
                                if (isDarkTheme) R.drawable.instagram_w else R.drawable.instagram_b,
                                !leadInstagramUrl.isNullOrBlank() && leadContact == ContactTypeEnum.SOCIAL.getField()
                            ) {
                                val textFromClipboard = clipboardManager.getText()
                                if (textFromClipboard != null) {
                                    val instagramUrl: String = textFromClipboard.toString()
                                    if (instagramUrl.startsWith("https://www.instagram.com/")) {
                                        onSetLeadInstagramUrl(instagramUrl)
                                        Toast.makeText(
                                            localContext,
                                            "Instagram URL copied and linked from clipboard",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                                onSetLeadContact(ContactTypeEnum.SOCIAL.getField())
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LittleBodyText("Tap on the Instagram icon to copy the profile url from your clipboard. The app will store the profile url")
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        CounterColumn(
                            count = if (leadAge != 0L) leadAge.toString() else "NA",
                            label = if (leadAge != 0L) "Years old" else "Age",
                            onIncrement = {
                                var newLeadAge = if (leadAge != 0L) leadAge + 1 else 20
                                onSetLeadAge(newLeadAge.toString())
                            },
                            onDecrement = {
                                var newLeadAge = if (leadAge != 0L) leadAge - 1 else 20
                                onSetLeadAge(newLeadAge.toString())
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            ConfirmButton {
                if (isUpdatingLead) {
                    lead.id = leadId
                    lead.insertTime = leadInsertTime
                }
                if (isUpdatingLead || saveLeadToLiveSession) {
                    lead.sessionId = leadSessionId
                }
                lead.name = leadName
                lead.contact = leadContact
                lead.nationality = leadNationality
                lead.age = leadAge
                lead.contactLookupKey = leadContactLookupKey
                lead.instagramUrl = leadInstagramUrl
                if (isUpdatingLead || saveLeadToLiveSession) {
                    onSaveLead(lead)
                } else {
                    onSetLead(lead)
                }
                onPostConfirm()
                val notificationLink =
                    if (ContactTypeEnum.SOCIAL.equals(lead.contact) && lead.instagramUrl != null) lead.instagramUrl!! else Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        lead.contactLookupKey
                    ).toString()
                if (writeHerAfterReminderEnabled && onScheduleReminder != null) {
                    onScheduleReminder(
                        writeHerReminderInterval,
                        lead.name + " " + CountryEnum.getFlagByAlpha3(lead.nationality),
                        notificationLink
                    )
                }
                if (isUpdatingLead || saveLeadToLiveSession) {
                    Toast.makeText(context, "Lead saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Lead on hold", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onDismiss()
            }
        })
}