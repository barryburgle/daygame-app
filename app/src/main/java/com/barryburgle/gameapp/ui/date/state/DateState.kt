package com.barryburgle.gameapp.ui.date.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.state.OrderState

data class DateState(
    // TODO: make this state and Input state inherit from class that hold the common fields like date, startHour, endHour, stickingPoints
    val dates: List<Date> = emptyList(),
    val allLeads: List<Lead> = emptyList(),
    val isAddingDate: Boolean = false,
    val isUpdatingDate: Boolean = false,
    override val sortType: DateSortType = DateSortType.DATE,
    val lead: Lead? = null,
    val editDate: Date? = null,
    var leadId: Long = 0L,
    var location: String = "",
    var date: String = "",
    var startHour: String = "",
    var endHour: String = "",
    var dateNumber: String = "0",
    var dateType: String = "",
    var cost: String = "0",
    var stickingPoints: String = "",
    var tweetUrl: String = "",
    var pull: Boolean = false,
    var bounce: Boolean = false,
    var kiss: Boolean = false,
    var lay: Boolean = false,
    var recorded: Boolean = false
) : OrderState(sortType)