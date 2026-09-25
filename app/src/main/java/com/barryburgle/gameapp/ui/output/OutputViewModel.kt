package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.AggregatedDatesDao
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.ping.PingDao
import com.barryburgle.gameapp.dao.ping.SentPingDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedSessionsDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.ping.SentPing
import com.barryburgle.gameapp.ui.CombineEighteen
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.utilities.dialog.passInitialValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class OutputViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val aggregatedSessionsDao: AggregatedSessionsDao,
    private val aggregatedDatesDao: AggregatedDatesDao,
    private val settingDao: SettingDao,
    private val leadDao: LeadDao,
    private val dateDao: DateDao,
    private val setDao: SetDao,
    private val pingDao: PingDao,
    private val sentPingDao: SentPingDao,
) : ViewModel() {
    private val _state = MutableStateFlow(OutputState())
    private val _allSessions = abstractSessionDao.getAll()
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _allSet = setDao.getAll()
    private val _allPing = pingDao.getAll()
    private val _allSentPing = sentPingDao.getAll()
    private val _sessionsByWeek = aggregatedSessionsDao.groupStatsByWeekNumber()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _sessionsByMonth = aggregatedSessionsDao.groupStatsByMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _datesByWeek = aggregatedDatesDao.groupStatsByWeekNumber()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _datesByMonth = aggregatedDatesDao.groupStatsByMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _lastSessionsShown = settingDao.getLastSessionsShown()
    private val _lastWeeksShown = settingDao.getLastWeeksShown()
    private val _lastMonthsShown = settingDao.getLastMonthsShown()
    private val _averageLast = settingDao.getAverageLast()
    private val _suggestLeadsNationality = settingDao.getSuggestLeadsNationality()
    private val _shownNationalities = settingDao.getShownNationalities()
    private val _mostPopularLeadsNationalities = leadDao.getNationalityHistogram()

    val state = CombineEighteen(
        _state,
        _allSessions,
        _allLeads,
        _allDates,
        _allSet,
        _allPing,
        _allSentPing,
        _sessionsByWeek,
        _sessionsByMonth,
        _datesByWeek,
        _datesByMonth,
        _averageLast,
        _lastSessionsShown,
        _lastWeeksShown,
        _lastMonthsShown,
        _suggestLeadsNationality,
        _shownNationalities,
        _mostPopularLeadsNationalities
    ) { state, allSessions, allLeads, allDates, allSets, allPings, allSentPings, sessionsByWeek, sessionsByMonth, datesByWeek, datesByMonth, averageLast, lastSessionsShown, lastWeeksShown, lastMonthsShown, suggestLeadsNationality, shownNationalities, mostPopularLeadsNationalities ->
        state.copy(
            allSessions = SessionManager.normalizeSessionsIds(allSessions),
            allLeads = allLeads,
            allDates = allDates,
            allSets = allSets,
            allSentPings = allSentPings,
            allPings = allPings,
            sessionsByWeek = sessionsByWeek,
            sessionsByMonth = sessionsByMonth,
            datesByWeek = datesByWeek,
            datesByMonth = datesByMonth,
            movingAverageWindow = averageLast,
            lastSessionsShown = lastSessionsShown,
            lastWeeksShown = lastWeeksShown,
            lastMonthsShown = lastMonthsShown,
            suggestLeadsNationality = suggestLeadsNationality.toBoolean(),
            shownNationalities = shownNationalities.toInt(),
            mostPopularLeadsNationalities = mostPopularLeadsNationalities
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutputState())


    fun onEvent(event: OutputEvent) {
        when (event) {

            is OutputEvent.SwitchShowLeadLegend -> {
                _state.update {
                    it.copy(
                        showLeadsLegend = _state.value.showLeadsLegend.not()
                    )
                }
            }

            is OutputEvent.SwitchShowIndexFormula -> {
                _state.update {
                    it.copy(
                        showIndexFormula = _state.value.showIndexFormula.not()
                    )
                }
            }


            is OutputEvent.SwitchShowCustomSummaryDialog -> {
                _state.update {
                    it.copy(
                        showCustomSummaryDialog = _state.value.showCustomSummaryDialog.not()
                    )
                }
            }

            is OutputEvent.EditLead -> {
                _state.update {
                    it.copy(
                        isUpdatingLead = event.isUpdatingLead,
                        leadId = event.lead.id ?: 0L,
                        leadInsertTime = event.lead.insertTime,
                        leadSessionId = event.lead.sessionId,
                        leadName = event.lead.name,
                        leadContact = event.lead.contact,
                        leadNationality = event.lead.nationality,
                        leadAge = event.lead.age,
                        leadContactLookupKey = event.lead.contactLookupKey,
                        leadInstagramUrl = event.lead.instagramUrl,
                        isInOverlay = true
                    )
                }
            }

            is OutputEvent.HideLeadDialog -> {
                _state.update {
                    it.copy(
                        isUpdatingLead = false,
                        isInOverlay = false,
                        leadName = "",
                        leadContact = "",
                        leadNationality = "",
                        countrySearch = "",
                        leadAge = 0,
                        leadContactLookupKey = null,
                        leadInstagramUrl = null
                    )
                }
            }

            is OutputEvent.SetIsInOverlayToTrue -> {
                _state.update {
                    it.copy(isInOverlay = true)
                }
            }

            is OutputEvent.SetIsInOverlayToFalse -> {
                _state.update {
                    it.copy(isInOverlay = false)
                }
            }

            is OutputEvent.SetLeadName -> {
                _state.update {
                    it.copy(leadName = event.name)
                }
            }

            is OutputEvent.SetLeadCountrySearch -> {
                _state.update {
                    it.copy(countrySearch = event.countrySearch)
                }
            }

            is OutputEvent.SetLeadNationality -> {
                _state.update {
                    it.copy(leadNationality = event.nationality)
                }
            }

            is OutputEvent.SetLeadContact -> {
                _state.update {
                    it.copy(leadContact = event.contact)
                }
            }

            is OutputEvent.SetLeadContactLookupKey -> {
                _state.update {
                    it.copy(leadContactLookupKey = event.contactLookupKey)
                }
            }

            is OutputEvent.SetLeadInstagramUrl -> {
                _state.update {
                    it.copy(leadInstagramUrl = event.instagramUrl)
                }
            }

            is OutputEvent.SetLeadAge -> {
                _state.update {
                    it.copy(leadAge = event.age.toLong())
                }
            }

            is OutputEvent.SaveLead -> {
                viewModelScope.launch {
                    leadDao.insert(event.lead)
                }
                _state.update {
                    it.copy(
                        isUpdatingLead = false,
                        isInOverlay = false,
                        leadName = "",
                        leadContact = "",
                        leadNationality = "",
                        countrySearch = "",
                        leadAge = 0,
                        leadContactLookupKey = null,
                        leadInstagramUrl = null
                    )
                }
            }

            is OutputEvent.DeleteLead -> {
                viewModelScope.launch {
                    leadDao.deleteById(event.leadId)
                }
                _state.update {
                    it.copy(
                        isUpdatingLead = false,
                        isInOverlay = false,
                        leadName = "",
                        leadContact = "",
                        leadNationality = "",
                        countrySearch = "",
                        leadAge = 0,
                        leadContactLookupKey = null,
                        leadInstagramUrl = null
                    )
                }
            }

            is OutputEvent.ShowFlightControlDialog -> {
                _state.update {
                    it.copy(
                        showFlightControlDialog = true
                    )
                }
            }

            is OutputEvent.HideFlightControlDialog -> {
                _state.update {
                    it.copy(
                        showFlightControlDialog = false
                    )
                }
            }

            is OutputEvent.ShowPingEditDialog -> {
                _state.update {
                    it.copy(
                        showAddPingDialog = true
                    )
                }
            }

            is OutputEvent.HidePingEditDialog -> {
                _state.update {
                    it.copy(
                        showAddPingDialog = false,
                        showEditPingDialog = false
                    )
                }
            }

            is OutputEvent.SavePing -> {
                // TODO: do a pings backup on any save of the pings
                viewModelScope.launch {
                    pingDao.insert(event.ping)
                }
            }

            is OutputEvent.EditPing -> {
                _state.update {
                    it.copy(
                        editPing = event.ping,
                        showEditPingDialog = true
                    )
                }
            }

            is OutputEvent.DeletePing -> {
                viewModelScope.launch {
                    pingDao
                        .deleteById(event.pingId)
                }
                _state.update {
                    it.copy(
                        showAddPingDialog = false
                    )
                }
            }

            is OutputEvent.WriteSentPing -> {
                val dateTime = passInitialValue(true, null, "")
                if (event.sent == true) {
                    val sentPing = SentPing(
                        leadId = event.leadId,
                        pingId = event.pingId,
                        sentHour = dateTime,
                        reaction = null
                    )
                    viewModelScope.launch {
                        sentPingDao
                            .insert(sentPing)
                    }
                } else {
                    viewModelScope.launch {
                        sentPingDao
                            .deleteByPingAndLead(event.pingId, event.leadId)
                    }
                }
            }
        }
    }
}