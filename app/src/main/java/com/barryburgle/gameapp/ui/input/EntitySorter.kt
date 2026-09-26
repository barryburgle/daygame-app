package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.ChallengeSortType
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.GameEventSortType
import com.barryburgle.gameapp.model.enums.SessionSortType
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.ScrollableSelector
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.selection.ScrollableSorter

@Composable
fun EntitySorter(
    visibilityFlag: Boolean,
    spaceFromLeft: Dp,
    eventType: EventTypeEnum,
    state: InputState,
    onEvent: (GameEvent) -> Unit
) {
    BasicAnimatedVisibility(
        visibilityFlag = visibilityFlag
    ) {
        ScrollableSorter(
            spaceFromLeft = spaceFromLeft
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Sort,
                contentDescription = "Sort By",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.height(25.dp)
            )
            Spacer(modifier = Modifier.width(spaceFromLeft))

            when (eventType) {
                EventTypeEnum.SESSION -> {
                    ScrollableSelector(
                        values = SessionSortType.entries,
                        selected = state.sessionSortType
                    ) { sortType ->
                        onEvent(GameEvent.SortSessions(sortType))
                    }
                }

                EventTypeEnum.SET -> {
                    ScrollableSelector(
                        values = SetSortType.entries,
                        selected = state.setSortType
                    ) { sortType ->
                        onEvent(GameEvent.SortSets(sortType))
                    }
                }

                EventTypeEnum.DATE -> {
                    ScrollableSelector(
                        values = DateSortType.entries,
                        selected = state.dateSortType
                    ) { sortType ->
                        onEvent(GameEvent.SortDates(sortType))
                    }
                }

                EventTypeEnum.CHALLENGE -> {
                    ScrollableSelector(
                        values = ChallengeSortType.entries,
                        selected = state.challengeSortType
                    ) { sortType ->
                        onEvent(GameEvent.SortChallenges(sortType))
                    }
                }

                EventTypeEnum.ALL -> {
                    ScrollableSelector(
                        values = GameEventSortType.entries,
                        selected = state.gameEventSortType
                    ) { sortType ->
                        onEvent(GameEvent.SortGameEvents(sortType))
                    }
                }
            }
        }
    }
}