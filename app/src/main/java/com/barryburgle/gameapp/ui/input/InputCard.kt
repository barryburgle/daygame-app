package com.barryburgle.gameapp.ui.input

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.FormatService
import java.time.DayOfWeek
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun InputCard(
    abstractSession: AbstractSession,
    leads: List<Lead>,
    onEvent: (AbstractSessionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val countFontSize = 50.sp
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
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
                                abstractSession.date
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
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Session date",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            // TODO: specify if session or set once single sets will be available in session screen
                            Text(
                                text = "Session",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier.width(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                    onEvent(
                                        AbstractSessionEvent.DeleteSession(
                                            abstractSession
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Session",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(30.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                    leads.forEach { lead ->
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
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Session",
                                        tint = MaterialTheme.colorScheme.inversePrimary,
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "${
                            DayOfWeek.of(abstractSession.dayOfWeek).toString().lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        } ${
                            FormatService.getTime(
                                abstractSession.startHour
                            )
                        } - ${
                            FormatService.getTime(
                                abstractSession.endHour
                            )
                        } : ${abstractSession.sessionTime} minutes",
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            describedQuantifier(
                                quantity = "${abstractSession.sets}",
                                quantityFontSize = countFontSize,
                                description = "Sets",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${abstractSession.convos}",
                                quantityFontSize = countFontSize,
                                description = "Conversations",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${abstractSession.contacts}",
                                quantityFontSize = countFontSize,
                                description = "Contacts",
                                descriptionFontSize = descriptionFontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            describedQuantifier(
                                quantity = "${FormatService.getPerc(abstractSession.convoRatio)}",
                                quantityFontSize = perfFontSize,
                                description = "Conversation\nRatio",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${FormatService.getPerc(abstractSession.contactRatio)}",
                                quantityFontSize = perfFontSize,
                                description = "Contact\nRatio",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${abstractSession.index}",
                                quantityFontSize = perfFontSize,
                                description = "Index",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${abstractSession.approachTime}",
                                quantityFontSize = perfFontSize,
                                description = "Minutes\nper set",
                                descriptionFontSize = descriptionFontSize
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(7.dp)
                            ) {
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
                                                            lead,
                                                            true
                                                        )
                                                    )
                                                    onEvent(
                                                        AbstractSessionEvent.ShowLeadDialog(
                                                            false,
                                                            false
                                                        )
                                                    )
                                                },
                                                horizontalArrangement = Arrangement.spacedBy(7.dp)
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
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(7.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    val stickingPoints =
                                        if (abstractSession.stickingPoints.isBlank()) "No sticking points" else abstractSession.stickingPoints
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .background(
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(7.dp)
                                            .clickable {
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
                                    ) {
                                        Text(
                                            text = "Sticking Points:",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = stickingPoints,
                                            style = MaterialTheme.typography.bodyMedium
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
}


@Composable
fun describedQuantifier(
    quantity: String, quantityFontSize: TextUnit, description: String, descriptionFontSize: TextUnit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier.padding(5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = quantity, fontSize = quantityFontSize
                )
                Text(
                    text = description,
                    fontSize = descriptionFontSize,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}