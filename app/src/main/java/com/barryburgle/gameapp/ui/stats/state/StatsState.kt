package com.barryburgle.gameapp.ui.stats.state

import com.barryburgle.gameapp.model.session.AbstractSession

data class StatsState(
    val abstractSessions: List<AbstractSession> = emptyList()
)