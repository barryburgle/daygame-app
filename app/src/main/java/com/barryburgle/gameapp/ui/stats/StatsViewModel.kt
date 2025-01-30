package com.barryburgle.gameapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.ui.stats.state.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(StatsState())
    private val _abstractSessions = abstractSessionDao.getAll()
    val state =
        combine(
            _state,
            _abstractSessions,
        ) { state, abstractSessions ->
            state.copy(
                abstractSessions = abstractSessions,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsState())
}