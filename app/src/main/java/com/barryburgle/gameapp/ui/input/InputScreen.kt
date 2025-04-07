package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import com.barryburgle.gameapp.ui.utilities.ScrollableSorter
import com.barryburgle.gameapp.ui.utilities.SelectionRow
import com.barryburgle.gameapp.ui.utilities.selection.MultiChoiceButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    // TODO: integrate on the right a scrollbar (mainly invisible) that allows to easily jump to a session around a certain date
    val spaceFromNavBar = 80.dp
    val selectedOptions = remember {
        mutableStateListOf(true, true, true)
    }
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
            LeadDialog(state = state, onEvent = onEvent, "Add a lead")
        }
        if (state.isModifyingLead) {
            LeadDialog(state = state, onEvent = onEvent, "Modify the lead")
        }
        if (state.isUpdatingLead) {
            LeadDialog(state = state, onEvent = onEvent, "Update the lead")
        }
        if (state.justSaved && state.backupActive) {
            runBlocking {
                async {
                    DataExchangeService.backup(state)
                }
            }
            onEvent(AbstractSessionEvent.SwitchJustSaved)
        }
        InsertInvite(state.allSessions, "Session", MaterialTheme.typography.titleLarge)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop + spaceFromLeft
                ),
            verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(spaceFromLeft)
                    ) {}
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Sort By",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(25.dp)
                    )
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    MultiChoiceButton(
                        EventTypeEnum.getAllFields(),
                        Modifier.fillMaxWidth(0.95f),
                        selectedOptions
                    ) {
                        onEvent(AbstractSessionEvent.SwitchShowFlag(it))
                    }
                }
            }
            item {
                // TODO: make the following values change accordingly to the type of entity selected by MultiChoiceButton
                ScrollableSorter(
                    spaceFromLeft
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Sort,
                        contentDescription = "Sort By",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(25.dp)
                    )
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    SortType.values().forEach { sortType ->
                        state.sortType?.let {
                            SelectionRow(
                                it, sortType, onEvent as (GenericEvent) -> Unit,
                                AbstractSessionEvent.SortSessions(
                                    sortType
                                )
                            )
                        }
                    }
                }
            }
            items(state.allEvents) { sortableGameEvent ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    EventCard(
                        sortableGameEvent,
                        getLeads(state, sortableGameEvent),
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
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom * 2 + spaceFromLeft * 3)) {} }
        }
    }
}

fun getLeads(state: InputState, sortableGameEvent: SortableGameEvent): List<Lead> {
    if (AbstractSession::class.java.simpleName.equals(sortableGameEvent.classType)) {
        val abstractSession = sortableGameEvent.event as AbstractSession
        return state.allLeads.filter { lead -> lead.sessionId == abstractSession.id }
    }
    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
        val date = sortableGameEvent.event as Date
        return state.allLeads.filter { lead -> lead.id == date.leadId }
    }
    return emptyList()
}