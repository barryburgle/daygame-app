package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.ui.CombineEight
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToolViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(ToolsState())
    private val _abstractSessionHeader = AbstractSession.header()
    private val _abstractSessions = abstractSessionDao.getAll()
    private val _exportFilename = settingDao.getExportFilename()
    private val _importFilename = settingDao.getImportFilename()
    private val _exportFolder = settingDao.getExportFolder()
    private val _notificationTime = settingDao.getNotificationTime()
    private val _averageLast = settingDao.getAverageLast()
    private val _lastSession = abstractSessionDao.getLastSession()
    val state =
        CombineEight(
            _state,
            _abstractSessions,
            _exportFilename,
            _importFilename,
            _exportFolder,
            _notificationTime,
            _averageLast,
            _lastSession
        ) { state, abstractSessions, exportFilename, importFilename, exportFolder, notificationTime, averageLast, lastSession ->
            state.copy(
                exportFileName = exportFilename,
                importFileName = importFilename,
                exportFolder = exportFolder,
                notificationTime = notificationTime,
                abstractSessionHeader = _abstractSessionHeader,
                abstractSessions = abstractSessions,
                lastSessionAverageQuantity = averageLast,
                lastSessionDate = lastSession.date,
                lastSessionStickingPoints = lastSession.stickingPoints
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
                val exportFileName = _state.value.exportFileName
                val setting = Setting(SettingDao.EXPORT_FILE_NAME_ID, exportFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportFileName -> {
                _state.update {
                    it.copy(
                        importFileName = event.importFileName
                    )
                }
                val importFileName = _state.value.importFileName
                val setting = Setting(SettingDao.IMPORT_FILE_NAME_ID, importFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetExportFolder -> {
                _state.update {
                    it.copy(
                        exportFolder = event.exportFolder
                    )
                }
                val exportFolder = _state.value.exportFolder
                val setting = Setting(SettingDao.EXPORT_FOLDER_ID, exportFolder)
                viewModelScope.launch { settingDao.insert(setting) }
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
                val lastSessionAverageQuantity = _state.value.lastSessionAverageQuantity
                val setting = Setting(
                    SettingDao.LAST_SESSION_AVERAGE_QUANTITY_ID,
                    lastSessionAverageQuantity.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetNotificationTime -> {
                _state.update {
                    it.copy(
                        notificationTime = event.notificationTime
                    )
                }
                val notificationTime = _state.value.notificationTime
                val setting = Setting(SettingDao.NOTIFICATION_TIME_ID, notificationTime)
                viewModelScope.launch { settingDao.insert(setting) }
            }
        }
    }
}