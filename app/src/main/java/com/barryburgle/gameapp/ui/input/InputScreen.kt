package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.state.InputState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState, onEvent: (AbstractSessionEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(AbstractSessionEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Session")
            }
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        if (state.isAddingSession) {
            AddInputDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier.clickable {
                                onEvent(
                                    AbstractSessionEvent.SortSessions(
                                        sortType
                                    )
                                )
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = state.sortType == sortType, onClick = {
                                onEvent(
                                    AbstractSessionEvent.SortSessions(
                                        sortType
                                    )
                                )
                            })
                            Text(text = sortType.field)
                        }
                    }
                }
            }
            items(state.abstractSessions) { abstractSession ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sets: ${abstractSession.sets} - Convos: ${abstractSession.convos} - Contacts: ${abstractSession.contacts}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Convo Ratio: ${abstractSession.convoRatio} - Rejection Ratio: ${abstractSession.rejectionRatio} - Contact Ratio: ${abstractSession.contactRatio}",
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Index: ${abstractSession.index}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Day: ${FormatService.getDate(abstractSession.date)}",
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Start: ${FormatService.getTime(abstractSession.startHour)} - End: ${
                                FormatService.getTime(
                                    abstractSession.endHour
                                )
                            }",
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(
                            AbstractSessionEvent.DeleteSession(
                                abstractSession
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Session"
                        )
                    }
                }
            }
        }
    }
}
