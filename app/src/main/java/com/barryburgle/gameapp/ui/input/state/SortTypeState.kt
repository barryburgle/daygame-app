package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.enums.ChallengeSortType
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.GameEventSortType
import com.barryburgle.gameapp.model.enums.SessionSortType
import com.barryburgle.gameapp.model.enums.SetSortType


data class SortTypeState(
    var sessionSortType: SessionSortType = SessionSortType.DATE,
    var dateSortType: DateSortType = DateSortType.DATE,
    var setSortType: SetSortType = SetSortType.DATE,
    var challengeSortType: ChallengeSortType = ChallengeSortType.END_DATE,
    var gameEventSortType: GameEventSortType = GameEventSortType.DATE,
)
