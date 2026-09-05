package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.stat.AggregatedDates
import com.barryburgle.gameapp.model.stat.AggregatedSessions
import com.barryburgle.gameapp.model.stat.CategoryHistogram
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
    val lastSessionsShown: Int = 8,
    val lastWeeksShown: Int = 8,
    val lastMonthsShown: Int = 4,
    val showLeadsLegend: Boolean = false,
    val showIndexFormula: Boolean = false,
    val showCustomSummaryDialog: Boolean = false,
    val isUpdatingLead: Boolean = false,
    val leadId: Long = 0L,
    val leadInsertTime: String = "",
    val leadSessionId: Long? = 0L,
    val leadName: String = "",
    val leadContact: String = "",
    val leadNationality: String = "",
    val leadAge: Long = 20,
    val leadContactLookupKey: String? = null,
    val leadInstagramUrl: String? = null,
    val countrySearch: String = "",
    val mostPopularLeadsNationalities: List<CategoryHistogram> = emptyList(),
    val suggestLeadsNationality: Boolean = true,
    val shownNationalities: Int = 6,
    val isInOverlay: Boolean = false
) : AllEntityState(
    allSessions,
    allLeads,
    allDates,
    allSets
)