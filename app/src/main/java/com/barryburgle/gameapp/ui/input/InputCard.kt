package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.FormatService
import java.time.DayOfWeek

@ExperimentalMaterial3Api
@Composable
fun InputCard(
    abstractSession: AbstractSession,
    onEvent: (AbstractSessionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val countFontSize = 50.sp
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Session ${FormatService.getDate(abstractSession.date)}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(0.85f),
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
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(30.dp)
                                )
                            ) {
                                IconButton(onClick = {
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
                                        modifier = Modifier.height(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "${FormatService.getTime(abstractSession.startHour)} - ${
                            FormatService.getTime(
                                abstractSession.endHour
                            )
                        } on ${
                            DayOfWeek.of(abstractSession.dayOfWeek).toString().lowercase()
                        } (${abstractSession.sessionTime} minutes)",
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
                        Spacer(modifier = Modifier.height(13.dp))
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
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .background(
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(7.dp)
                                    ) {
                                        Text(
                                            text = "Sticking Points:",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        val stickingPoints =
                                            if (abstractSession.stickingPoints.isBlank()) "No sticking points" else abstractSession.stickingPoints
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