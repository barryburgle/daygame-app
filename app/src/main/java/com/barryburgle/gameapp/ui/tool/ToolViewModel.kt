package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun onEvent(event: ToolEvent) {
        when (event) {
            is ToolEvent.SetExportFileName -> {
                _state.update {
                    it.copy(
                        exportFileName = event.exportFileName
                    )
                }
            }

            is ToolEvent.SetImportFileName -> {
                _state.update {
                    it.copy(
                        importFileName = event.importFileName
                    )
                }
            }

            is ToolEvent.SetExportFolder -> {
                _state.update {
                    it.copy(
                        exportFolder = event.exportFolder
                    )
                }
            }

            is ToolEvent.SetAbstractSessions -> {
                _state.update {
                    it.copy(
                        abstractSessions = event.abstractSessions
                    )
                }
                val abstractSessions = _state.value.abstractSessions
                viewModelScope.launch { abstractSessionDao.batchInsert(abstractSessions) }
            }
        }
    }
}