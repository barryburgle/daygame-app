package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.ui.CombineEight
import com.barryburgle.gameapp.ui.CombineTwentyOne
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToolViewModel(
    private val abstractSessionDao: AbstractSessionDao,
    private val leadDao: LeadDao,
    private val dateDao: DateDao,
    private val setDao: SetDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(ToolsState())
    private val _allSessions = abstractSessionDao.getAll()
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _allSets = setDao.getAll()
    val _importExportFilenames: Flow<ImportExportFilenames> = CombineEight(
        settingDao.getExportSessionsFilename(),
        settingDao.getImportSessionsFilename(),
        settingDao.getExportLeadsFilename(),
        settingDao.getImportLeadsFilename(),
        settingDao.getExportDatesFilename(),
        settingDao.getImportDatesFilename(),
        settingDao.getExportSetsFilename(),
        settingDao.getImportSetsFilename()
    ) { exportSessions, importSessions, exportLeads, importLeads, exportDates, importDates, exportSets, importSets ->
        ImportExportFilenames(
            exportSessionsFilename = exportSessions,
            importSessionsFilename = importSessions,
            exportLeadsFilename = exportLeads,
            importLeadsFilename = importLeads,
            exportDatesFilename = exportDates,
            importDatesFilename = importDates,
            exportSetsFilename = exportSets,
            importSetsFilename = importSets
        )
    }
    private val _exportFolder = settingDao.getExportFolder()
    private val _importFolder = settingDao.getImportFolder()
    private val _backupFolder = settingDao.getBackupFolder()
    private val _exportHeader = settingDao.getExportHeaderFlag()
    private val _importHeader = settingDao.getImportHeaderFlag()
    private val _backupActive = settingDao.getBackupActiveFlag()
    private val _notificationTime = settingDao.getNotificationTime()
    private val _averageLast = settingDao.getAverageLast()
    private val _latestAvailable = settingDao.getLatestAvailable()
    private val _latestPublishDate = settingDao.getLatestPublishDate()
    private val _latestChangelog = settingDao.getLatestChangelog()
    private val _latestDownloadUrl = settingDao.getLatestDownloadUrl()
    private val _lastSessionsShown = settingDao.getLastSessionsShown()
    private val _lastWeeksShown = settingDao.getLastWeeksShown()
    private val _lastMonthsShown = settingDao.getLastMonthsShown()
    val state =
        CombineTwentyOne(
            _state,
            _allSessions,
            _allLeads,
            _allDates,
            _allSets,
            _importExportFilenames,
            _exportFolder,
            _importFolder,
            _backupFolder,
            _notificationTime,
            _averageLast,
            _exportHeader,
            _importHeader,
            _backupActive,
            _latestAvailable,
            _latestPublishDate,
            _latestChangelog,
            _latestDownloadUrl,
            _lastSessionsShown,
            _lastWeeksShown,
            _lastMonthsShown
        ) { state, allSessions, allLeads, allDates, allSets, importExportFilenames, exportFolder, importFolder, backupFolder, notificationTime, averageLast, exportHeader, importHeader, backupActive, latestAvailable, latestPublishDate, latestChangelog, latestDownloadUrl, lastSessionsShown, lastWeeksShown, lastMonthsShown ->
            state.copy(
                exportSessionsFileName = importExportFilenames.exportSessionsFilename,
                importSessionsFileName = importExportFilenames.importSessionsFilename,
                exportLeadsFileName = importExportFilenames.exportLeadsFilename,
                importLeadsFileName = importExportFilenames.importLeadsFilename,
                exportDatesFileName = importExportFilenames.exportDatesFilename,
                importDatesFileName = importExportFilenames.importDatesFilename,
                exportSetsFileName = importExportFilenames.exportSetsFilename,
                importSetsFileName = importExportFilenames.importSetsFilename,
                exportFolder = exportFolder,
                importFolder = importFolder,
                backupFolder = backupFolder,
                notificationTime = notificationTime,
                allSessions = allSessions,
                allLeads = allLeads,
                allDates = allDates,
                allSets = allSets,
                lastSessionAverageQuantity = averageLast,
                exportHeader = exportHeader.toBoolean(),
                importHeader = importHeader.toBoolean(),
                backupActive = backupActive.toBoolean(),
                latestAvailable = latestAvailable,
                latestPublishDate = latestPublishDate,
                latestChangelog = latestChangelog,
                latestDownloadUrl = latestDownloadUrl,
                lastSessionsShown = lastSessionsShown,
                lastWeeksShown = lastWeeksShown,
                lastMonthsShown = lastMonthsShown
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

            is ToolEvent.SetExportDatesFileName -> {
                _state.update {
                    it.copy(
                        exportDatesFileName = event.exportDatesFileName
                    )
                }
                val exportDatesFileName = _state.value.exportDatesFileName
                val setting = Setting(SettingDao.EXPORT_DATES_FILE_NAME_ID, exportDatesFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportDatesFileName -> {
                _state.update {
                    it.copy(
                        importDatesFileName = event.importDatesFileName
                    )
                }
                val importDatesFileName = _state.value.importDatesFileName
                val setting = Setting(SettingDao.IMPORT_DATES_FILE_NAME_ID, importDatesFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetExportSetsFileName -> {
                _state.update {
                    it.copy(
                        exportSetsFileName = event.exportSetsFileName
                    )
                }
                val exportSetsFileName = _state.value.exportSetsFileName
                val setting = Setting(SettingDao.EXPORT_SETS_FILE_NAME_ID, exportSetsFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportSetsFileName -> {
                _state.update {
                    it.copy(
                        importSetsFileName = event.importSetsFileName
                    )
                }
                val importSetsFileName = _state.value.importSetsFileName
                val setting = Setting(SettingDao.IMPORT_SETS_FILE_NAME_ID, importSetsFileName)
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

            is ToolEvent.SetBackupFolder -> {
                _state.update {
                    it.copy(
                        backupFolder = event.backupFolder
                    )
                }
                val backupFolder = _state.value.backupFolder
                val setting = Setting(SettingDao.BACKUP_FOLDER_ID, backupFolder)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetAllSessions -> {
                _state.update {
                    it.copy(
                        allSessions = event.allSessions
                    )
                }
                val allSessions = _state.value.allSessions
                viewModelScope.launch { abstractSessionDao.batchInsert(allSessions) }
            }

            is ToolEvent.SetAllLeads -> {
                _state.update {
                    it.copy(
                        allLeads = event.allLeads
                    )
                }
                val allLeads = _state.value.allLeads
                viewModelScope.launch { leadDao.batchInsert(allLeads) }
            }

            is ToolEvent.SetAllDates -> {
                _state.update {
                    it.copy(
                        allDates = event.allDates
                    )
                }
                val allDates = _state.value.allDates
                viewModelScope.launch { dateDao.batchInsert(allDates) }
            }

            is ToolEvent.SetAllSets -> {
                _state.update {
                    it.copy(
                        allSets = event.allSets
                    )
                }
                val allSets = _state.value.allSets
                viewModelScope.launch { setDao.batchInsert(allSets) }
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

            is ToolEvent.SetLastSessionsShown -> {
                _state.update {
                    it.copy(
                        lastSessionsShown = event.lastSessionsShown.toInt()
                    )
                }
                val lastSessionsShown = _state.value.lastSessionsShown
                val setting = Setting(
                    SettingDao.LAST_SESSIONS_SHOWN_ID,
                    lastSessionsShown.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetLastWeeksShown -> {
                _state.update {
                    it.copy(
                        lastWeeksShown = event.lastWeeksShown.toInt()
                    )
                }
                val lastWeeksShown = _state.value.lastWeeksShown
                val setting = Setting(
                    SettingDao.LAST_WEEKS_SHOWN_ID,
                    lastWeeksShown.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetLastMonthsShown -> {
                _state.update {
                    it.copy(
                        lastMonthsShown = event.lastMonthsShown.toInt()
                    )
                }
                val lastMonthsShown = _state.value.lastMonthsShown
                val setting = Setting(
                    SettingDao.LAST_MONTHS_SHOWN_ID,
                    lastMonthsShown.toString()
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

            is ToolEvent.SwitchBackupBeforeUpdate -> {
                _state.update {
                    it.copy(
                        backupBeforeUpdate = _state.value.backupBeforeUpdate.not()
                    )
                }
            }

            is ToolEvent.SwitchBackupActive -> {
                _state.update {
                    it.copy(
                        backupActive = _state.value.backupActive.not()
                    )
                }
                val backupActive = _state.value.backupActive
                val setting = Setting(SettingDao.BACKUP_ACTIVE_ID, backupActive.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetLastBackup -> {
                _state.update {
                    it.copy(
                        lastBackup = event.lastBackup.toInt()
                    )
                }
                val lastBackup = _state.value.lastBackup
                val setting = Setting(SettingDao.BACKUP_NUMBER_ID, lastBackup.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchShowChangelog -> {
                _state.update {
                    it.copy(
                        showChangelog = _state.value.showChangelog.not()
                    )
                }
            }

            is ToolEvent.SwitchBackupBeforeUpdate -> {
                _state.update {
                    it.copy(
                        backupBeforeUpdate = _state.value.backupBeforeUpdate.not()
                    )
                }
            }
        }
    }
}

data class ImportExportFilenames(
    val exportSessionsFilename: String,
    val importSessionsFilename: String,
    val exportLeadsFilename: String,
    val importLeadsFilename: String,
    val exportDatesFilename: String,
    val importDatesFilename: String,
    val exportSetsFilename: String,
    val importSetsFilename: String,
)