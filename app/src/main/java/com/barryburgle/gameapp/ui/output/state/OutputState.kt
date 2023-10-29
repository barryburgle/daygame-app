package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.stat.WeekStat
import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.model.session.AbstractSession

data class OutputState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    val weekStats: List<WeekStat> = emptyList(),
    val chartType: ChartType = ChartType.ABSOLUTE
)