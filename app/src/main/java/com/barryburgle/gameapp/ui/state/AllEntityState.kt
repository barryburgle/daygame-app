package com.barryburgle.gameapp.ui.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet

open class AllEntityState(
    open var allSessions: List<AbstractSession> = emptyList(),
    open var allLeads: List<Lead> = emptyList(),
    open var allDates: List<Date> = emptyList(),
    open var allSets: List<SingleSet> = emptyList()
)