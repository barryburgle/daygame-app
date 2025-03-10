package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import com.barryburgle.gameapp.ui.utilities.ScrollableSorter
import com.barryburgle.gameapp.ui.utilities.SelectionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
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
            SessionDialog(state = state, onEvent = onEvent, "Add a Session")
        }
        if (state.isUpdatingSession) {
            SessionDialog(state = state, onEvent = onEvent, "Edit a Session")
        }
        if (state.isAddingLead) {
            AddLeadDialog(state = state, onEvent = onEvent, "Add a lead")
        }
        if (state.isModifyingLead) {
            AddLeadDialog(state = state, onEvent = onEvent, "Modify the lead")
        }
        if (state.isUpdatingLead) {
            AddLeadDialog(state = state, onEvent = onEvent, "Update the lead")
        }
        InsertInvite(state.abstractSessions, "Session")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop + spaceFromLeft
                ),
            verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                ScrollableSorter(
                    spaceFromLeft
                ) {
                    SortType.values().forEach { sortType ->
                        state.sortType?.let {
                            SelectionRow(
                                it, sortType, onEvent as (GenericEvent) -> Unit
                            )
                        }
                    }
                }
            }
            items(state.abstractSessions) { abstractSession ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    InputCard(
                        abstractSession,
                        state.allLeads.filter { lead -> lead.sessionId == abstractSession.id },
                        onEvent,
                        Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
                            .shadow(
                                elevation = 5.dp,
                                shape = MaterialTheme.shapes.large
                            )
                    )
                }
            }
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)) {} }
        }
    }
}