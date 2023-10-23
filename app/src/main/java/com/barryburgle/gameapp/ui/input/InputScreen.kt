package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.ui.input.state.InputState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState, onEvent: (AbstractSessionEvent) -> Unit
) {
    val spaceFromTop = 80.dp
    val spaceFromBottom = 60.dp
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AbstractSessionEvent.ShowDialog) },
                modifier = Modifier.offset(y = -spaceFromTop)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a session"
                )
            }
        }
    ) { padding ->
        if (state.isAddingSession) {
            AddInputDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort by: ",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.offset(y = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 10.dp)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SortType.values().forEach { sortType ->
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onEvent(
                                            AbstractSessionEvent.SortSessions(
                                                sortType
                                            )
                                        )
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.sortType == sortType, onClick = {
                                        onEvent(
                                            AbstractSessionEvent.SortSessions(
                                                sortType
                                            )
                                        )
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary,
                                        unselectedColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                                Text(
                                    text = sortType.field + " ",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.abstractSessions) { abstractSession ->
                InputCard(
                    abstractSession,
                    onEvent,
                    Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 5.dp,
                            shape = MaterialTheme.shapes.large
                        )
                )
            }
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
        }
    }
}
