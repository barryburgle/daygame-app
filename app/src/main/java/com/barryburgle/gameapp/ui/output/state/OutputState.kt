package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.AggregatedStat

data class OutputState(
    val chartType: ChartType = ChartType.SESSION,
    val abstractSessions: List<AbstractSession> = emptyList(),
    val weekStats: List<AggregatedStat> = emptyList(),
    val monthStats: List<AggregatedStat> = emptyList(),
    val movingAverageWindow: Int = 4
)