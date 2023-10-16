package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
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
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
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
                        tint = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "[${abstractSession.sessionTime}] minutes - Start: ${
                    FormatService.getTime(
                        abstractSession.startHour
                    )
                } - End: ${FormatService.getTime(abstractSession.endHour)}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${abstractSession.sets} Sets - ${abstractSession.convos} Conversations - ${abstractSession.contacts} Contacts",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Index: ${abstractSession.index} - Approach Time: ${abstractSession.approachTime}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sticking Points:\n${abstractSession.stickingPoints}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Conversation Ratio: ${abstractSession.convoRatio}\nRejection Ratio: ${abstractSession.rejectionRatio}\nContact Ratio: ${abstractSession.contactRatio}\nWeekday: ${
                    DayOfWeek.of(
                        abstractSession.dayOfWeek
                    )
                }",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
