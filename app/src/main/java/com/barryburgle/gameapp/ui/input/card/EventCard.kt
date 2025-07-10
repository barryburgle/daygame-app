package com.barryburgle.gameapp.ui.input.card

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.card.body.DateBody
import com.barryburgle.gameapp.ui.input.card.body.SessionBody
import com.barryburgle.gameapp.ui.input.card.body.SetBody
import com.barryburgle.gameapp.ui.input.dialog.leadName
import com.barryburgle.gameapp.ui.utilities.button.ShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.body.MediumBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@ExperimentalMaterial3Api
@Composable
fun EventCard(
    sortableGameEvent: SortableGameEvent,
    leads: List<Lead>,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    LittleBodyText(
                        "${
                            FormatService.getDate(
                                sortableGameEvent.event.getEventDate()
                            )
                        }"
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = sortableGameEvent.event.getEventIcon(),
                                contentDescription = "Session date",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            LargeTitleText(sortableGameEvent.event.getEventTitle())
                        }
                        Row(
                            modifier = Modifier.width(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ShadowButton(
                                onClick = {
                                    if (AbstractSession::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.DeleteSession(
                                                sortableGameEvent.event as AbstractSession
                                            )
                                        )
                                    }
                                    if (SingleSet::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.DeleteSet(
                                                sortableGameEvent.event as SingleSet
                                            )
                                        )
                                    }
                                    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        onEvent(
                                            GameEvent.DeleteDate(
                                                sortableGameEvent.event as Date
                                            )
                                        )
                                    }
                                },
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Event",
                                iconColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                            ShadowButton(
                                onClick = {
                                    onEvent(GameEvent.EmptyLeads)
                                    if (AbstractSession::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        leads.forEach { lead ->
                                            onEvent(
                                                GameEvent.SetLead(
                                                    lead
                                                )
                                            )
                                        }
                                        onEvent(
                                            GameEvent.EditSession(
                                                sortableGameEvent.event as AbstractSession
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.SESSION
                                            )
                                        )
                                    }
                                    if (SingleSet::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.SetConversation((sortableGameEvent.event as SingleSet).conversation)
                                        )
                                        onEvent(
                                            GameEvent.SetContact((sortableGameEvent.event as SingleSet).contact)
                                        )
                                        onEvent(
                                            GameEvent.SetInstantDate((sortableGameEvent.event as SingleSet).instantDate)
                                        )
                                        onEvent(
                                            GameEvent.SetRecorded((sortableGameEvent.event as SingleSet).recorded)
                                        )
                                        onEvent(
                                            GameEvent.EditSet(
                                                sortableGameEvent.event as SingleSet
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.SET
                                            )
                                        )
                                    }
                                    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        onEvent(
                                            GameEvent.SetPull((sortableGameEvent.event as Date).pull)
                                        )
                                        onEvent(
                                            GameEvent.SetBounce((sortableGameEvent.event as Date).bounce)
                                        )
                                        onEvent(
                                            GameEvent.SetKiss((sortableGameEvent.event as Date).kiss)
                                        )
                                        onEvent(
                                            GameEvent.SetLay((sortableGameEvent.event as Date).lay)
                                        )
                                        onEvent(
                                            GameEvent.SetRecorded((sortableGameEvent.event as Date).recorded)
                                        )
                                        onEvent(
                                            GameEvent.EditDate(
                                                sortableGameEvent.event as Date
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.DATE
                                            )
                                        )
                                    }
                                },
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Event"
                            )
                        }
                    }
                    LittleBodyText(sortableGameEvent.event.getEventDescription())
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        EventCardSection {
                            if (AbstractSession::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                SessionBody(
                                    sortableGameEvent.event as AbstractSession,
                                    50.sp,
                                    10.sp,
                                    15.sp
                                )
                            }
                            if (SingleSet::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                SetBody(sortableGameEvent.event as SingleSet, 10.sp, 15.sp)
                            }
                            if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                DateBody(sortableGameEvent.event as Date, 10.sp, 15.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        EventCardSection {
                            if (leads == null || leads.isEmpty()) {
                                LittleBodyText("No leads")
                            } else {
                                LittleBodyText("Leads:")
                                Spacer(modifier = Modifier.height(7.dp))
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (lead in leads) {
                                        item {
                                            Spacer(modifier = Modifier.width(5.dp))
                                        }
                                        item {
                                            Row(
                                                modifier = Modifier.clickable {
                                                    onEvent(
                                                        GameEvent.EditLead(
                                                            lead, true
                                                        )
                                                    )
                                                    onEvent(
                                                        GameEvent.ShowLeadDialog(
                                                            false, false
                                                        )
                                                    )
                                                },
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    7.dp
                                                )
                                            ) {
                                                leadName(
                                                    lead = lead,
                                                    backgroundColor = MaterialTheme.colorScheme.background,
                                                    outputShow = false,
                                                    cardShow = true
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            var stickingPoints = sortableGameEvent.event.getEventStickingPoints()
                            val validStickingPoints =
                                stickingPoints != null && !stickingPoints.isBlank()
                            var width = 1f
                            if (validStickingPoints) {
                                width = 0.85f
                            }
                            EventCardSection(width = width) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    ) {
                                        if (stickingPoints == null || stickingPoints.isBlank()) {
                                            LittleBodyText("No sticking points")
                                        } else {
                                            LittleBodyText("Sticking points:")
                                            Spacer(modifier = Modifier.height(5.dp))
                                            MediumBodyText(if (stickingPoints != null) stickingPoints else "No sticking points")
                                        }
                                    }
                                }
                            }
                            if (validStickingPoints) {
                                ShadowButton(
                                    onClick = {
                                        if (stickingPoints != null) {
                                            clipboardManager.setText(
                                                AnnotatedString(
                                                    stickingPoints
                                                )
                                            )
                                            Toast.makeText(
                                                localContext,
                                                "Sticking points copied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Sticking Points"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}