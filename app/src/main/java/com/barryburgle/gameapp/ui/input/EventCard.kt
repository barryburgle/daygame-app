package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService

@ExperimentalMaterial3Api
@Composable
fun EventCard(
    sortableGameEvent: SortableGameEvent,
    leads: List<Lead>,
    // TODO: the following should be a generic Input/GameEvent -> Unit
    onEvent: (AbstractSessionEvent) -> Unit,
    modifier: Modifier = Modifier
    // TODO: pass here the body function
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
                    Text(
                        text = "${
                            FormatService.getDate(
                                sortableGameEvent.event.getEventDate()
                            )
                        }",
                        style = MaterialTheme.typography.bodySmall
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
                            Text(
                                text = sortableGameEvent.event.getEventTitle(),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier.width(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(50.dp)
                                )
                            ) {
                                IconButton(onClick = {
                                    // TODO: define the following
                                    /*onEvent(
                                        AbstractSessionEvent.DeleteSession(
                                            abstractSession
                                        )
                                    )*/
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Session",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(30.dp)
                                )
                            ) {
                                IconButton(onClick = {
                                    onEvent(AbstractSessionEvent.EmptyLeads)
                                    // TODO: define the following
                                    /*leads.forEach { lead ->
                                        onEvent(
                                            AbstractSessionEvent.SetLead(
                                                lead
                                            )
                                        )
                                    }
                                    onEvent(
                                        AbstractSessionEvent.EditSession(
                                            abstractSession
                                        )
                                    )
                                    onEvent(
                                        AbstractSessionEvent.ShowDialog(false, true)
                                    )*/
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Session",
                                        tint = MaterialTheme.colorScheme.inversePrimary,
                                        modifier = Modifier.height(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = sortableGameEvent.event.getEventDescription(),
                        style = MaterialTheme.typography.bodySmall
                    )
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
                        // TODO: insert here body function
                        EventCardSection {
                            if (leads == null || leads.isEmpty()) {
                                Text(
                                    text = "No leads",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "Leads:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(5.dp))
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
                                                        AbstractSessionEvent.EditLead(
                                                            lead, true
                                                        )
                                                    )
                                                    onEvent(
                                                        AbstractSessionEvent.ShowLeadDialog(
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
                        Spacer(modifier = Modifier.height(5.dp))
                        EventCardSection {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                val stickingPoints =
                                    sortableGameEvent.event.getEventStickingPoints()
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .fillMaxHeight()
                                ) {
                                    if (stickingPoints == null || stickingPoints.isBlank()) {
                                        Text(
                                            text = "No sticking points",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    } else {
                                        Text(
                                            text = "Sticking points:",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = if (stickingPoints != null) stickingPoints else "No sticking points",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                if (stickingPoints != null && !stickingPoints.isBlank()) {
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
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
                                        contentDescription = "Copy Sticking Points",
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}