package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.service.EntityService
import com.barryburgle.gameapp.ui.input.CounterColumn
import com.barryburgle.gameapp.ui.input.dialog.text.DialogTextComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.output.LeadCard
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.DialogTimeFormSection
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
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    var setsCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.sets
    var convosCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.convos
    var contactsCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.contacts
    var setsCount by remember {
        mutableStateOf(if (setsCountStart == null) 0 else setsCountStart)
    }
    var convosCount by remember {
        mutableStateOf(if (convosCountStart == null) 0 else convosCountStart)
    }
    var contactsCount by remember {
        mutableStateOf(if (contactsCountStart == null) 0 else contactsCountStart)
    }
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(GameEvent.SetIsInOverlayToFalse)
            onEvent(GameEvent.HideDialog)
        },
        title = {
            LargeTitleText(description)
        },
        text = {
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
                                    "Set session's:", DialogConstant.DESCRIPTION_FONT_SIZE
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(
                                modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                            ) {
                                DialogFormSectionDescription(
                                    "Add leads:", DialogConstant.DESCRIPTION_FONT_SIZE
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
                                    // TODO: the blur stys set on when inserting a lead on the session and then getting back to game events view
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DialogTimeFormSection(
                        state, onEvent, latestDateValue, latestStartHour, latestEndHour
                    )
                    Column(
                        modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH),
                        verticalArrangement = Arrangement.Top
                    ) {
                        for (lead in state.leads) {
                            Box(modifier = Modifier.clickable {
                                Toast.makeText(
                                    localContext,
                                    "Please edit the lead from session card",
                                    Toast.LENGTH_LONG
                                ).show()
                            }) {
                                LeadCard(
                                    lead = lead,
                                    onEditClick = {}, // Dropping any edit lead support from session dialog -> The user should be able to edit the lead by the LeadCard edit button
                                    onLinkClick = {},
                                    shortCut = true
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(5.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CounterColumn(count = setsCount.toString(), label = "Sets", onIncrement = {
                        setsCount++
                        onEvent(GameEvent.SetSets(setsCount.toString()))
                    }, onDecrement = {
                        setsCount--
                        onEvent(GameEvent.SetSets(setsCount.toString()))
                    })
                    CounterColumn(
                        count = convosCount.toString(),
                        label = "Conversations",
                        onIncrement = {
                            convosCount++
                            onEvent(GameEvent.SetConvos(convosCount.toString()))
                            if (state.followCount) {
                                setsCount++
                            }
                        },
                        onDecrement = {
                            convosCount--
                            onEvent(GameEvent.SetConvos(convosCount.toString()))
                        })
                    CounterColumn(
                        count = contactsCount.toString(),
                        label = "Contacts",
                        onIncrement = {
                            contactsCount++
                            onEvent(GameEvent.SetContacts(contactsCount.toString()))
                            if (state.followCount) {
                                setsCount++
                                convosCount++
                            }
                        },
                        onDecrement = {
                            contactsCount--
                            onEvent(GameEvent.SetContacts(contactsCount.toString()))
                        })
                }
                Spacer(modifier = Modifier.height(7.dp))
                val stickingPoints = state.stickingPoints
                DialogTextComponent(
                    value = state.stickingPoints,
                    placeholder = "sticking points",
                    singleLine = false,
                    onCopyClick = {
                        clipboardManager.setText(
                            AnnotatedString(
                                stickingPoints
                            )
                        )
                        Toast.makeText(
                            localContext, "Sticking points copied", Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    onEvent(GameEvent.SetStickingPoints(it))
                }
            }
        },
        confirmButton = {
            ConfirmButton {
                if (EntityService.getParsedHour(
                        state.date, state.startHour
                    ) > EntityService.getParsedHour(state.date, state.endHour)
                ) {
                    Toast.makeText(
                        localContext, "Please choose valid hours", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onEvent(GameEvent.SaveAbstractSession)
                    onEvent(GameEvent.SetIsInOverlayToFalse)
                    onEvent(GameEvent.HideDialog)
                    onEvent(GameEvent.SwitchJustSaved)
                    Toast.makeText(
                        localContext, "Session saved", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideDialog)
            }
        })
}