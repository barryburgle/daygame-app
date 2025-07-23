package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.InputCounter
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.DialogTimeFormSection
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    var setsCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.sets
    var convosCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.convos
    var contactsCountStart =
        if (state.isAddingSession) 0 else state.editAbstractSession?.contacts
    var setsCount by remember {
        mutableStateOf(if (setsCountStart == null) 0 else setsCountStart)
    }
    var convosCount by remember {
        mutableStateOf(if (convosCountStart == null) 0 else convosCountStart)
    }
    var contactsCount by remember {
        mutableStateOf(if (contactsCountStart == null) 0 else contactsCountStart)
    }
    if (state.isUpdatingSession) {
        setState(state)
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.HideDialog)
    }, title = {
        LargeTitleText(description)
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
                                "Set session's:",
                                DialogConstant.DESCRIPTION_FONT_SIZE
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                        ) {
                            DialogFormSectionDescription(
                                "Add leads:",
                                DialogConstant.DESCRIPTION_FONT_SIZE
                            )
                        }
                        Column(
                            modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                        ) {
                            IconShadowButton(
                                onClick = {
                                    onEvent(GameEvent.ShowLeadDialog(true, false))
                                    if (state.followCount) {
                                        setsCount++
                                        convosCount++
                                        contactsCount++
                                        onEvent(GameEvent.SetContacts(contactsCount.toString()))
                                    }
                                },
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add a lead"
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DialogTimeFormSection(
                    state,
                    onEvent,
                    latestDateValue,
                    latestStartHour,
                    latestEndHour
                )
                Column(
                    modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH),
                    verticalArrangement = Arrangement.Top
                ) {
                    for (lead in state.leads) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.clickable {
                                onEvent(GameEvent.EditLead(lead, true))
                                onEvent(
                                    GameEvent.ShowLeadDialog(false, true)
                                )
                            }) {
                                leadName(
                                    lead = lead,
                                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                    outputShow = false,
                                    cardShow = false
                                )
                            }
                            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                                IconButton(onClick = {
                                    onEvent(
                                        GameEvent.DeleteLead(
                                            lead
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Lead",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LittleBodyText("Sets")
                    IconShadowButton(
                        onClick = {
                            setsCount--
                            onEvent(GameEvent.SetSets(setsCount.toString()))
                        },
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Less"
                    )
                    InputCounter(
                        count = setsCount,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                    )
                    IconShadowButton(
                        onClick = {
                            setsCount++
                            onEvent(GameEvent.SetSets(setsCount.toString()))
                        },
                        imageVector = Icons.Default.Add,
                        contentDescription = "More"
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LittleBodyText("Conversations")
                    IconShadowButton(
                        onClick = {
                            convosCount--
                            onEvent(GameEvent.SetConvos(convosCount.toString()))
                        },
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Less"
                    )
                    InputCounter(
                        count = convosCount,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                    )
                    IconShadowButton(
                        onClick = {
                            convosCount++
                            onEvent(GameEvent.SetConvos(convosCount.toString()))
                            if (state.followCount) {
                                setsCount++
                            }
                        },
                        imageVector = Icons.Default.Add,
                        contentDescription = "More"
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LittleBodyText("Contacts")
                    IconShadowButton(
                        onClick = {
                            contactsCount--
                            onEvent(GameEvent.SetContacts(contactsCount.toString()))
                        },
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Less"
                    )
                    InputCounter(
                        count = contactsCount,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                    )
                    IconShadowButton(
                        onClick = {
                            contactsCount++
                            onEvent(GameEvent.SetContacts(contactsCount.toString()))
                            if (state.followCount) {
                                setsCount++
                                convosCount++
                            }
                        },
                        imageVector = Icons.Default.Add,
                        contentDescription = "More"
                    )
                }
            }
            OutlinedTextField(
                value = state.stickingPoints,
                onValueChange = { onEvent(GameEvent.SetStickingPoints(it)) },
                placeholder = { LittleBodyText("Sticking Points") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.height(100.dp)
            )
        }
    }, confirmButton = {
        // TODO. use this everywhere in all te dialogs
        ConfirmButton {
            onEvent(GameEvent.SaveAbstractSession)
            onEvent(GameEvent.HideDialog)
            onEvent(GameEvent.SwitchJustSaved)
            Toast.makeText(
                localContext,
                "Session saved",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }, dismissButton = {
        DismissButton {
            onEvent(GameEvent.HideDialog)
        }
    }
    )
}

private fun setState(
    state: InputState
) {
    if (state.editAbstractSession != null) {
        state.date = state.editAbstractSession.date.substring(0, 10)
        state.startHour = state.editAbstractSession.startHour.substring(11, 16)
        state.endHour = state.editAbstractSession.endHour.substring(11, 16)
        state.sets = state.editAbstractSession.sets.toString()
        state.convos = state.editAbstractSession.convos.toString()
        state.contacts = state.editAbstractSession.contacts.toString()
        if (state.stickingPoints.isBlank()) {
            state.stickingPoints = state.editAbstractSession.stickingPoints
        }
    }
}

@Composable
fun leadName(
    lead: Lead,
    backgroundColor: Color,
    alertColor: Color? = null,
    outputShow: Boolean,
    cardShow: Boolean
) {
    var displayName = lead.name
    if (displayName != null && !displayName.isBlank()) {
        if (displayName.length > 7) {
            displayName = displayName.substring(0, 5) + "... "
        }
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = MaterialTheme.shapes.large
                ),
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = backgroundColor, shape = RoundedCornerShape(5.dp)
                    )
                    .width(80.dp)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
                if (outputShow && alertColor != null) {
                    textColor = MaterialTheme.colorScheme.onPrimary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(alertColor, shape = RoundedCornerShape(5.dp))
                    ) {}
                    Spacer(Modifier.height(3.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // TODO: create MediumBodyText with variable injectable color
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                if (outputShow || cardShow) {
                    // TODO: create MediumBodyText with variable injectable color
                    Text(
                        text = "${lead.age} ${
                            CountryEnum.getFlagByAlpha3(
                                lead.nationality
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                    if (lead.contact == ContactTypeEnum.NUMBER.getField()) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.whatsapp_w),
                                contentDescription = "Whatsapp Icon",
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(textColor)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.instagram_w),
                                contentDescription = "Instagram Icon",
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(textColor)
                            )
                        }
                    }
                    if (!cardShow) {
                        if (lead.insertTime.isNotBlank()) {
                            LittleBodyText(
                                "${lead.insertTime.substring(8, 10)}/${
                                    lead.insertTime.substring(5, 7)
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}
