package com.barryburgle.gameapp.ui.stats.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram

data class StatsState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    val leads: List<Lead> = emptyList(),
    val dates: List<Date> = emptyList(),
    val setsHistogram: List<Histogram> = emptyList(),
    val convosHistogram: List<Histogram> = emptyList(),
    val contactsHistogram: List<Histogram> = emptyList(),
    val nationalityHistogram: List<CategoryHistogram> = emptyList(),
    val ageHistogram: List<Histogram> = emptyList(),
)