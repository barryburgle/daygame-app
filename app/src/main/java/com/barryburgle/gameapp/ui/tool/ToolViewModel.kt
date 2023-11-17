package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ToolViewModel(private val abstractSessionDao: AbstractSessionDao) : ViewModel() {
    private val _state = MutableStateFlow(ToolsState())
    private val _abstractSessionHeader = AbstractSession.header()
    private val _abstractSessions = abstractSessionDao.getAll()
    val state = combine(_state, _abstractSessions) { state, abstractSessions ->
        state.copy(
            abstractSessions = abstractSessions,
            abstractSessionHeader = _abstractSessionHeader
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ToolsState())

}