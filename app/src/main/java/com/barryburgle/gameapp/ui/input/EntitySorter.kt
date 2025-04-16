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
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.ScrollableSorter
import com.barryburgle.gameapp.ui.utilities.SelectionRow

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
            spaceFromLeft
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Sort,
                contentDescription = "Sort By",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.height(25.dp)
            )
            Spacer(modifier = Modifier.width(spaceFromLeft))
            if (EventTypeEnum.SESSION.equals(eventType)) {
                SortType.values().forEach { sortType ->
                    state.sortType?.let {
                        SelectionRow(
                            it,
                            sortType,
                            onEvent as (GenericEvent) -> Unit,
                            GameEvent.SortSessions(
                                sortType
                            )
                        )
                    }
                }
            } else if (EventTypeEnum.SET.equals(eventType)) {
                SetSortType.values().forEach { sortType ->
                    state.sortType?.let {
                        SelectionRow(
                            it,
                            sortType,
                            onEvent as (GenericEvent) -> Unit,
                            GameEvent.SortSets(
                                sortType
                            )
                        )
                    }
                }
            } else if (EventTypeEnum.DATE.equals(eventType)) {
                DateSortType.values().forEach { sortType ->
                    state.sortType?.let {
                        SelectionRow(
                            it,
                            sortType,
                            onEvent as (GenericEvent) -> Unit,
                            GameEvent.SortDates(
                                sortType
                            )
                        )
                    }
                }
            }
        }
    }
}

