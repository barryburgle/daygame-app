package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.stat.AggregatedDates
import com.barryburgle.gameapp.model.stat.AggregatedSessions
import com.barryburgle.gameapp.ui.state.AllEntityState

data class OutputState(
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    override var allSets: List<SingleSet> = emptyList(),
    val sessionsByWeek: List<AggregatedSessions> = emptyList(),
    val sessionsByMonth: List<AggregatedSessions> = emptyList(),
    val datesByWeek: List<AggregatedDates> = emptyList(),
    val datesByMonth: List<AggregatedDates> = emptyList(),
    val movingAverageWindow: Int = 4,
    val showLeadsLegend: Boolean = false,
    val showIndexFormula: Boolean = false
) : AllEntityState(
    allSessions,
    allLeads,
    allDates,
    allSets
)