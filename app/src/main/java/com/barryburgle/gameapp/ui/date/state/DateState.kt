package com.barryburgle.gameapp.ui.date.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.ui.state.OrderState

data class DateState(
    val dates: List<Date> = emptyList(),
    override val sortType: DateSortType = DateSortType.DATE
) : OrderState(sortType)