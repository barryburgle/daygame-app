package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedStatDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ChartTypeEvent
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.ui.CombineSix
import com.barryburgle.gameapp.ui.output.state.OutputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class OutputViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val aggregatedStatDao: AggregatedStatDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state = MutableStateFlow(OutputState())
    private val _chartType = MutableStateFlow(ChartType.SESSION)
    private val _abstractSessions = MutableStateFlow(abstractSessionDao.getAllLimit(14))
    private val _weekStats = MutableStateFlow(
        aggregatedStatDao.groupStatsByWeekNumber()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    )
    private val _monthStats = MutableStateFlow(
        aggregatedStatDao.groupStatsByMonth()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    )
    private val _averageLast = MutableStateFlow(
        settingDao.getAverageLast()
    )

    // TODO: create a dao method to get last n sessions and then let user set n from tools screen
    val state = CombineSix(
        _state,
        _abstractSessions.value,
        _weekStats.value,
        _monthStats.value,
        _chartType,
        _averageLast.value
    )
    { state, abstractSessions, weekStats, monthStats, chartType, averageLast ->
        state.copy(
            abstractSessions = SessionManager.normalizeSessionsIds(abstractSessions),
            weekStats = SessionManager.normalizeIds(weekStats),
            monthStats = SessionManager.normalizeIds(monthStats),
            chartType = chartType,
            movingAverageWindow = averageLast
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutputState())


    fun onEvent(event: ChartTypeEvent) {
        when (event) {
            is ChartTypeEvent.SortCharts -> {
                _chartType.value = event.chartType
            }
        }
    }
}