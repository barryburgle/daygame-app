package com.barryburgle.gameapp.ui.output

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.ui.output.state.OutputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OutputViewModel(private val abstractSessionDao: AbstractSessionDao) : ViewModel() {
    private val _state = MutableStateFlow(OutputState())
    private val _chartType = MutableStateFlow(ChartType.ABSOLUTE)
    private val _abstractSessions = abstractSessionDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    // TODO: create a dao method to get last n sessions and then let user set n from tools screen
    val state =
        combine(_state, _chartType, _abstractSessions) { state, chartType, abstractSessions ->
            state.copy(
                abstractSessions = SessionManager.normalizeSessionsIds(abstractSessions),
                chartType = chartType
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutputState())
}