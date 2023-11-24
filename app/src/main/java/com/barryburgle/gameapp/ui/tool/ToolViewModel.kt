package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToolViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val settingDao: SettingDao
) : ViewModel() {
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

            is ToolEvent.SetLastSessionAverageQuantity -> {
                _state.update {
                    it.copy(
                        lastSessionAverageQuantity = event.lastSessionAverageQuantity.toInt()
                    )
                }
            }

            is ToolEvent.SetNotificationTime -> {
                _state.update {
                    it.copy(
                        notificationTime = event.notificationTime
                    )
                }
            }

            ToolEvent.SaveExportFileName -> viewModelScope.launch {
                val exportFileName = state.value.exportFileName
                val setting = Setting("export_file_name", exportFileName)
                viewModelScope.launch { settingDao.insert(setting) }
                _state.update { it.copy(exportFileName = exportFileName) }
            }

            ToolEvent.SaveImportFileName -> viewModelScope.launch {
                val importFileName = state.value.importFileName
                val setting = Setting("import_file_name", importFileName)
                viewModelScope.launch { settingDao.insert(setting) }
                _state.update { it.copy(importFileName = importFileName) }
            }

            ToolEvent.SaveExportFolder -> viewModelScope.launch {
                val exportFolder = state.value.exportFolder
                val setting = Setting("export_folder", exportFolder)
                viewModelScope.launch { settingDao.insert(setting) }
                _state.update { it.copy(exportFolder = exportFolder) }
            }

            ToolEvent.SaveLastSessionAverageQuantity -> viewModelScope.launch {
                val lastSessionAverageQuantity = state.value.lastSessionAverageQuantity
                val setting = Setting("average_last", lastSessionAverageQuantity.toString())
                viewModelScope.launch { settingDao.insert(setting) }
                _state.update { it.copy(lastSessionAverageQuantity = lastSessionAverageQuantity) }
            }

            ToolEvent.SaveNotificationTime -> viewModelScope.launch {
                val notificationTime = state.value.notificationTime
                val setting = Setting("notification_time", notificationTime)
                viewModelScope.launch { settingDao.insert(setting) }
                _state.update { it.copy(notificationTime = notificationTime) }
            }
        }
    }
}