package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.AggregatedDates
import com.barryburgle.gameapp.model.stat.AggregatedSessions

data class OutputState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    val leads: List<Lead> = emptyList(),
    val sessionsByWeek: List<AggregatedSessions> = emptyList(),
    val sessionsByMonth: List<AggregatedSessions> = emptyList(),
    val datesByWeek: List<AggregatedDates> = emptyList(),
    val datesByMonth: List<AggregatedDates> = emptyList(),
    val movingAverageWindow: Int = 4,
    val showLeadsLegend: Boolean = false,
    val showIndexFormula: Boolean = false
)