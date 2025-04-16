package com.barryburgle.gameapp.ui.input

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.input.card.EventCard
import com.barryburgle.gameapp.ui.input.dialog.DateDialog
import com.barryburgle.gameapp.ui.input.dialog.SessionDialog
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
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
    onEvent: (GameEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    // TODO: integrate on the right a scrollbar (mainly invisible) that allows to easily jump to a session around a certain date
    val spaceFromNavBar = 80.dp
    val selectedOptions = remember {
        mutableStateListOf(true, true, true)
    }
    var isRotated by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) -225f else 0f,
        animationSpec = tween(durationMillis = 650),
        label = "rotationAngle"
    )
    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.2f)
                            .offset(y = -100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        floatingAddButton(Icons.Default.Favorite, "date") {
                            onEvent(GameEvent.ShowDialog(true, false, EventTypeEnum.DATE))
                            isExpanded = false
                            isRotated = false
                        }
                        floatingAddButton(Icons.Default.PersonAddAlt1, "set") {
                            onEvent(GameEvent.ShowDialog(true, false, EventTypeEnum.SET))
                            isExpanded = false
                            isRotated = false
                        }
                        floatingAddButton(Icons.Default.GroupAdd, "session") {
                            onEvent(GameEvent.ShowDialog(true, false, EventTypeEnum.SESSION))
                            isExpanded = false
                            isRotated = false
                        }
                    }
                }
                FloatingActionButton(
                    onClick = {
                        isRotated = !isRotated
                        isExpanded = !isExpanded
                    },
                    modifier = Modifier
                        .offset(y = -spaceFromNavBar)
                        .rotate(rotationAngle),
                    contentColor = MaterialTheme.colorScheme.inversePrimary,
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add an event"
                    )
                }
            }
        }
    ) { padding ->
        if (state.isAddingSession) {
            SessionDialog(state = state, onEvent = onEvent, "Add a session")
        }
        if (state.isUpdatingSession) {
            SessionDialog(state = state, onEvent = onEvent, "Edit a session")
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
        if (state.isAddingDate) {
            DateDialog(state = state, onEvent = onEvent, "Add a date")
        }
        if (state.isUpdatingDate) {
            DateDialog(state = state, onEvent = onEvent, "Edit a date")
        }
        if (state.justSaved && state.backupActive) {
            runBlocking {
                async {
                    DataExchangeService.backup(state)
                }
            }
            onEvent(GameEvent.SwitchJustSaved)
        }
        InsertInvite(state.allSessions, "Session", MaterialTheme.typography.titleLarge)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop + spaceFromLeft
                ), verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MultiChoiceButton(
                        EventTypeEnum.getAllFields(),
                        listOf(state.allSessions.size, state.allSets.size, state.allDates.size),
                        Modifier.fillMaxWidth(0.95f),
                        selectedOptions
                    ) {
                        onEvent(GameEvent.SwitchShowFlag(it))
                    }
                }
            }
            item {
                var showSessionSorter: Boolean = false
                var showSetsSorter: Boolean = false
                var showDatesSorter: Boolean = false
                if (state.showSessions && !state.showSets && !state.showDates) {
                    showSessionSorter = true
                } else {
                    showSessionSorter = false
                }
                if (!state.showSessions && state.showSets && !state.showDates) {
                    showSetsSorter = true
                } else {
                    showSetsSorter = false
                }
                if (!state.showSessions && !state.showSets && state.showDates) {
                    showDatesSorter = true
                } else {
                    showDatesSorter = false
                }
                EntitySorter(
                    showSessionSorter,
                    spaceFromLeft,
                    EventTypeEnum.SESSION,
                    state,
                    onEvent
                )
                EntitySorter(
                    showSetsSorter,
                    spaceFromLeft,
                    EventTypeEnum.SET,
                    state,
                    onEvent
                )
                EntitySorter(
                    showDatesSorter,
                    spaceFromLeft,
                    EventTypeEnum.DATE,
                    state,
                    onEvent
                )
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

@Composable
fun floatingAddButton(
    icon: ImageVector, description: String, onClick: () -> Unit
) {
    val contentDescription = "Add a $description"
    // TODO: add tooltips below and fix
    /*Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.4f),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .offset(x = (-100).dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
                .padding(8.dp),
            text = contentDescription,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(8.dp))*/
    FloatingActionButton(
        onClick = {
            onClick()
        },
        modifier = Modifier.size(40.dp),
        contentColor = MaterialTheme.colorScheme.inversePrimary,
        containerColor = MaterialTheme.colorScheme.tertiary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = icon, contentDescription = contentDescription
        )
    }
}
//}