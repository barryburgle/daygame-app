package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.AggregatedDatesDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedSessionsDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.ui.CombineEight
import com.barryburgle.gameapp.ui.output.state.OutputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class OutputViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val aggregatedSessionsDao: AggregatedSessionsDao,
    private val aggregatedDatesDao: AggregatedDatesDao,
    private val settingDao: SettingDao,
    private val leadDao: LeadDao
) : ViewModel() {
    private val _state = MutableStateFlow(OutputState())

    private val _abstractSessions = abstractSessionDao.getAllLimit()
    private val _leads = leadDao.getAll()
    private val _sessionsByWeek = aggregatedSessionsDao.groupStatsByWeekNumber()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _sessionsByMonth = aggregatedSessionsDao.groupStatsByMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _datesByWeek = aggregatedDatesDao.groupStatsByWeekNumber()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _datesByMonth = aggregatedDatesDao.groupStatsByMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _averageLast = settingDao.getAverageLast()

    val state = CombineEight(
        _state,
        _abstractSessions,
        _leads,
        _sessionsByWeek,
        _sessionsByMonth,
        _datesByWeek,
        _datesByMonth,
        _averageLast
    )
    { state, abstractSessions, leads, sessionsByWeek, sessionsByMonth, datesByWeek, datesByMonth, averageLast ->
        state.copy(
            abstractSessions = SessionManager.normalizeSessionsIds(abstractSessions),
            leads = leads,
            sessionsByWeek = sessionsByWeek,
            sessionsByMonth = sessionsByMonth,
            datesByWeek = datesByWeek,
            datesByMonth = datesByMonth,
            movingAverageWindow = averageLast
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
        }
    }
}