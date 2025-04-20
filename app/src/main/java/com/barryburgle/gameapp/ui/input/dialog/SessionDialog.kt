package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.TimeInputFormEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.TimeInputFormButton
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    // TODO: make all the fields displayed in the dialog change when in edit mode (similarly to counters)
    val localContext = LocalContext.current.applicationContext
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    if (state.isUpdatingSession) {
        setState(state)
    }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.HideDialog)
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
                            IconButton(onClick = {
                                onEvent(GameEvent.ShowLeadDialog(true, false))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add a lead",
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
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.width(DialogConstant.TIME_COLUMN_WIDTH)
                ) {
                    TimeInputFormButton(
                        state.date,
                        latestDateValue,
                        TimeInputFormEnum.DATE,
                        state.isAddingSession,
                        state.editAbstractSession,
                        if (state.editAbstractSession != null) state.editAbstractSession!!.date else "",
                        "session",
                        ""
                    ) {
                        onEvent(GameEvent.SetDate(it))
                    }
                    TimeInputFormButton(
                        state.startHour,
                        latestStartHour,
                        TimeInputFormEnum.HOUR,
                        state.isAddingSession,
                        state.editAbstractSession,
                        if (state.editAbstractSession != null) state.editAbstractSession!!.startHour else "",
                        "session",
                        "Start"
                    ) {
                        onEvent(GameEvent.SetStartHour(it.substring(0, 5)))
                    }
                    TimeInputFormButton(
                        state.endHour,
                        latestEndHour,
                        TimeInputFormEnum.HOUR,
                        state.isAddingSession,
                        state.editAbstractSession,
                        if (state.editAbstractSession != null) state.editAbstractSession!!.endHour else "",
                        "session",
                        "End"
                    ) {
                        onEvent(GameEvent.SetEndHour(it.substring(0, 5)))
                    }
                }
                Column(
                    modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    for (lead in state.leads) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
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
                InputCountComponent(
                    inputTitle = "Sets",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.sets,
                    saveEvent = GameEvent::SetSets
                )
                InputCountComponent(
                    inputTitle = "Conversations",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.convos,
                    saveEvent = GameEvent::SetConvos
                )
                InputCountComponent(
                    inputTitle = "Contacts",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingSession) 0 else state.editAbstractSession?.contacts,
                    saveEvent = GameEvent::SetContacts
                )
            }
            OutlinedTextField(
                value = state.stickingPoints,
                onValueChange = { onEvent(GameEvent.SetStickingPoints(it)) },
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
                    onEvent(GameEvent.HideDialog)
                }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    onEvent(GameEvent.SaveAbstractSession)
                    onEvent(GameEvent.HideDialog)
                    onEvent(GameEvent.SwitchJustSaved)
                    Toast.makeText(localContext, "Session saved", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    Text(text = "Save")
                }
            }
        }
    })
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
            displayName = displayName.substring(0, 6) + "... "
        }
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 5.dp,
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
                if (outputShow && alertColor != null) {
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
                    Text(
                        text = "${displayName}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (outputShow || cardShow) {
                    Text(
                        text = "${lead.age} ${
                            CountryEnum.getFlagByAlpha3(
                                lead.nationality
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    val isDarkTheme = isSystemInDarkTheme()
                    if (lead.contact == ContactTypeEnum.NUMBER.getField()) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(if (isDarkTheme) R.drawable.whatsapp_w else R.drawable.whatsapp_b),
                                contentDescription = "Whatsapp Icon",
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(if (isDarkTheme) R.drawable.instagram_w else R.drawable.instagram_b),
                                contentDescription = "Instagram Icon",
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    if (!cardShow) {
                        if (lead.insertTime.isNotBlank()) {
                            Text(
                                style = MaterialTheme.typography.bodySmall,
                                text = "${lead.insertTime.substring(8, 10)}/${
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
