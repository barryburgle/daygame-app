package com.barryburgle.gameapp.ui.stats.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import com.barryburgle.gameapp.ui.state.AllEntityState

data class StatsState(
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    override var allSets: List<SingleSet> = emptyList(),
    val setsHistogram: List<Histogram> = emptyList(),
    val convosHistogram: List<Histogram> = emptyList(),
    val contactsHistogram: List<Histogram> = emptyList(),
    val leadsNationalityHistogram: List<CategoryHistogram> = emptyList(),
    val leadsAgeHistogram: List<Histogram> = emptyList(),
    val datesNationalityHistogram: List<CategoryHistogram> = emptyList(),
    val datesAgeHistogram: List<Histogram> = emptyList(),
    val datesNumberHistogram: List<Histogram> = emptyList()
) : AllEntityState(
    allSessions,
    allLeads,
    allDates,
    allSets
)