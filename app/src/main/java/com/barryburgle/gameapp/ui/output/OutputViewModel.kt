package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedStatDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.ui.CombineSix
import com.barryburgle.gameapp.ui.output.state.OutputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class OutputViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val aggregatedStatDao: AggregatedStatDao,
    private val settingDao: SettingDao,
    private val leadDao: LeadDao
) : ViewModel() {
    private val _state = MutableStateFlow(OutputState())
    // TODO: let user set n for following query from tools screen with a writing query on db
    private val _abstractSessions = abstractSessionDao.getAllLimit(14)
    private val _leads = leadDao.getAll()
    private val _weekStats = aggregatedStatDao.groupStatsByWeekNumber()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _monthStats = aggregatedStatDao.groupStatsByMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _averageLast = settingDao.getAverageLast()

    val state = CombineSix(
        _state,
        _abstractSessions,
        _leads,
        _weekStats,
        _monthStats,
        _averageLast
    )
    { state, abstractSessions, leads, weekStats, monthStats, averageLast ->
        state.copy(
            abstractSessions = SessionManager.normalizeSessionsIds(abstractSessions),
            leads = leads,
            weekStats = SessionManager.normalizeIds(weekStats),
            monthStats = SessionManager.normalizeIds(monthStats),
            movingAverageWindow = averageLast
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutputState())
}