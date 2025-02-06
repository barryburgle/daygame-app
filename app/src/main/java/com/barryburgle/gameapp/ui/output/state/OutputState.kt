package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.AggregatedStat

data class OutputState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    val leads: List<Lead> = emptyList(),
    val weekStats: List<AggregatedStat> = emptyList(),
    val monthStats: List<AggregatedStat> = emptyList(),
    val movingAverageWindow: Int = 4,
    val showLeadsLegend: Boolean = false,
    val showIndexFormula: Boolean = false
)