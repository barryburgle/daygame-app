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
import androidx.compose.ui.unit.Dp
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
    val perfFontSize = 25.sp
    val descriptionFontSize = 10.sp
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = "Session ${FormatService.getDate(abstractSession.date)}",
                        style = MaterialTheme.typography.titleLarge
                    )
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
                Row {
                    Column {
                        Text(
                            text = "${abstractSession.sessionTime} minutes",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Start: ${FormatService.getTime(abstractSession.startHour)}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "End: ${FormatService.getTime(abstractSession.endHour)}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Conversation Ratio: ${abstractSession.convoRatio}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Rejection Ratio: ${abstractSession.rejectionRatio}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Contact Ratio: ${abstractSession.contactRatio}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Weekday: ${DayOfWeek.of(abstractSession.dayOfWeek)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        describedQuantifier(
                            quantity = "${abstractSession.index}",
                            quantityFontSize = perfFontSize,
                            description = "Index",
                            descriptionFontSize = descriptionFontSize,
                            cornerRay = 4.dp
                        )
                        describedQuantifier(
                            quantity = "${abstractSession.approachTime}",
                            quantityFontSize = perfFontSize,
                            description = "Approach Time",
                            descriptionFontSize = descriptionFontSize,
                            cornerRay = 4.dp
                        )
                    }
                }
                if (!abstractSession.stickingPoints.isBlank()) {
                    Column(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.inversePrimary,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(7.dp)
                    ) {
                        Text(
                            text = "Sticking Points:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${abstractSession.stickingPoints}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                describedQuantifier(
                    quantity = "${abstractSession.sets}",
                    quantityFontSize = countFontSize,
                    description = "Sets",
                    descriptionFontSize = descriptionFontSize,
                    cornerRay = 4.dp
                )
                describedQuantifier(
                    quantity = "${abstractSession.convos}",
                    quantityFontSize = countFontSize,
                    description = "Conversations",
                    descriptionFontSize = descriptionFontSize,
                    cornerRay = 4.dp
                )
                describedQuantifier(
                    quantity = "${abstractSession.contacts}",
                    quantityFontSize = countFontSize,
                    description = "Contacts",
                    descriptionFontSize = descriptionFontSize,
                    cornerRay = 4.dp
                )
            }
        }
    }
}


@Composable
fun describedQuantifier(
    quantity: String,
    quantityFontSize: TextUnit,
    description: String,
    descriptionFontSize: TextUnit,
    cornerRay: Dp
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(5.dp)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = quantity,
                    fontSize = quantityFontSize
                )
                Text(
                    text = description,
                    fontSize = descriptionFontSize
                )
            }
        }
    }
}