package com.barryburgle.gameapp.ui.input

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.SelectionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState, onEvent: (AbstractSessionEvent) -> Unit
) {
    val spaceFromTop = 20.dp
    val spaceFromBottom = 60.dp
    val spaceFromNavBar = 80.dp
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AbstractSessionEvent.ShowDialog(true, false)) },
                modifier = Modifier.offset(y = -spaceFromNavBar),
                contentColor = MaterialTheme.colorScheme.inversePrimary,
                containerColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a session"
                )
            }
        }
    ) { padding ->
        if (state.isAddingSession) {
            AddInputDialog(state = state, onEvent = onEvent, "Add a Session")
        }
        if (state.isUpdatingSession) {
            AddInputDialog(state = state, onEvent = onEvent, "Edit a Session")
        }
        if (state.isAddingLead) {
            AddLeadDialog(state = state, onEvent = onEvent, "Add a lead")
        }
        if (state.isUpdatingLead) {
            AddLeadDialog(state = state, onEvent = onEvent, "Update the lead")
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
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
                        SelectionRow(
                            state.sortType,
                            sortType,
                            onEvent as (GenericEvent) -> Unit
                        )
                    }
                }
            }
            items(state.abstractSessions) { abstractSession ->
                InputCard(
                    abstractSession,
                    state.allLeads.filter { lead -> lead.sessionId == abstractSession.id },
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
