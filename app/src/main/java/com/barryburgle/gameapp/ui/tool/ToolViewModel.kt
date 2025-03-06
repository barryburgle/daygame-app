package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.ui.CombineSixteen
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToolViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(ToolsState())
    private val _abstractSessions = abstractSessionDao.getAll()
    private val _leads = leadDao.getAll()
    private val _exportSessionsFilename = settingDao.getExportSessionsFilename()
    private val _importSessionsFilename = settingDao.getImportSessionsFilename()
    private val _exportLeadsFilename = settingDao.getExportLeadsFilename()
    private val _importLeadsFilename = settingDao.getImportLeadsFilename()
    private val _exportFolder = settingDao.getExportFolder()
    private val _exportHeader = settingDao.getExportHeaderFlag()
    private val _importHeader = settingDao.getImportHeaderFlag()
    private val _notificationTime = settingDao.getNotificationTime()
    private val _averageLast = settingDao.getAverageLast()
    private val _latestAvailable = settingDao.getLatestAvailable()
    private val _latestPublishDate = settingDao.getLatestPublishDate()
    private val _latestChangelog = settingDao.getLatestChangelog()
    private val _latestDownloadUrl = settingDao.getLatestDownloadUrl()
    val state =
        CombineSixteen(
            _state,
            _abstractSessions,
            _leads,
            _exportSessionsFilename,
            _importSessionsFilename,
            _exportLeadsFilename,
            _importLeadsFilename,
            _exportFolder,
            _notificationTime,
            _averageLast,
            _exportHeader,
            _importHeader,
            _latestAvailable,
            _latestPublishDate,
            _latestChangelog,
            _latestDownloadUrl
        ) { state, abstractSessions, leads, exportSessionsFilename, importSessionsFilename, exportLeadsFilename, importLeadsFilename, exportFolder, notificationTime, averageLast, exportHeader, importHeader, latestAvailable, latestPublishDate, latestChangelog, latestDownloadUrl ->
            state.copy(
                exportSessionsFileName = exportSessionsFilename,
                importSessionsFileName = importSessionsFilename,
                exportLeadsFileName = exportLeadsFilename,
                importLeadsFileName = importLeadsFilename,
                exportFolder = exportFolder,
                notificationTime = notificationTime,
                abstractSessions = abstractSessions,
                leads = leads,
                lastSessionAverageQuantity = averageLast,
                exportHeader = exportHeader.toBoolean(),
                importHeader = importHeader.toBoolean(),
                latestAvailable = latestAvailable,
                latestPublishDate = latestPublishDate,
                latestChangelog = latestChangelog,
                latestDownloadUrl = latestDownloadUrl,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ToolsState())

    fun onEvent(event: ToolEvent) {
        when (event) {
            is ToolEvent.SetExportSessionsFileName -> {
                _state.update {
                    it.copy(
                        exportSessionsFileName = event.exportSessionsFileName
                    )
                }
                val exportSessionsFileName = _state.value.exportSessionsFileName
                val setting =
                    Setting(SettingDao.EXPORT_SESSIONS_FILE_NAME_ID, exportSessionsFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportSessionsFileName -> {
                _state.update {
                    it.copy(
                        importSessionsFileName = event.importSessionsFileName
                    )
                }
                val importSessionsFileName = _state.value.importSessionsFileName
                val setting =
                    Setting(SettingDao.IMPORT_SESSIONS_FILE_NAME_ID, importSessionsFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetExportLeadsFileName -> {
                _state.update {
                    it.copy(
                        exportLeadsFileName = event.exportLeadsFileName
                    )
                }
                val exportLeadsFileName = _state.value.exportLeadsFileName
                val setting = Setting(SettingDao.EXPORT_LEADS_FILE_NAME_ID, exportLeadsFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportLeadsFileName -> {
                _state.update {
                    it.copy(
                        importLeadsFileName = event.importLeadsFileName
                    )
                }
                val importLeadsFileName = _state.value.importLeadsFileName
                val setting = Setting(SettingDao.IMPORT_LEADS_FILE_NAME_ID, importLeadsFileName)
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

            is ToolEvent.SetImportFolder -> {
                _state.update {
                    it.copy(
                        importFolder = event.importFolder
                    )
                }
                val importFolder = _state.value.importFolder
                val setting = Setting(SettingDao.IMPORT_FOLDER_ID, importFolder)
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

            is ToolEvent.SetLeads -> {
                _state.update {
                    it.copy(
                        leads = event.leads
                    )
                }
                val leads = _state.value.leads
                viewModelScope.launch { leadDao.batchInsert(leads) }
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

            is ToolEvent.SetExportHeader -> {
                _state.update {
                    it.copy(
                        exportHeader = event.exportHeader
                    )
                }
                val exportHeader = _state.value.exportHeader
                val setting = Setting(SettingDao.EXPORT_HEADER_ID, exportHeader.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportHeader -> {
                _state.update {
                    it.copy(
                        importHeader = event.importHeader
                    )
                }
                val importHeader = _state.value.importHeader
                val setting = Setting(SettingDao.IMPORT_HEADER_ID, importHeader.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }
        }
    }
}