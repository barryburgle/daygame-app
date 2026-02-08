package com.barryburgle.gameapp.ui.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryburgle.gameapp.dao.challenge.ChallengeDao
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.ui.CombineEighteen
import com.barryburgle.gameapp.ui.CombineSeventeen
import com.barryburgle.gameapp.ui.CombineSixteen
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
    private val challengeDao: ChallengeDao,
    private val settingDao: SettingDao
) : ViewModel() {
    private val _state =
        MutableStateFlow(ToolsState())
    private val _allSessions = abstractSessionDao.getAll()
    private val _allLeads = leadDao.getAll()
    private val _allDates = dateDao.getAll()
    private val _allSets = setDao.getAll()
    private val _allChallenges = challengeDao.getAll()
    val _importExportSettingState: Flow<ImportExportSettingState> = CombineEighteen(
        settingDao.getExportFolder(),
        settingDao.getImportFolder(),
        settingDao.getBackupFolder(),
        settingDao.getExportHeaderFlag(),
        settingDao.getImportHeaderFlag(),
        settingDao.getBackupActiveFlag(),
        settingDao.getExportSessionsFilename(),
        settingDao.getImportSessionsFilename(),
        settingDao.getExportLeadsFilename(),
        settingDao.getImportLeadsFilename(),
        settingDao.getExportDatesFilename(),
        settingDao.getImportDatesFilename(),
        settingDao.getExportSetsFilename(),
        settingDao.getImportSetsFilename(),
        settingDao.getExportChallengesFilename(),
        settingDao.getImportChallengesFilename(),
        settingDao.getArchiveBackupFolder(),
        settingDao.getIsCleaning()
    ) { exportFolder, importFolder, backupFolder, exportHeader, importHeader, backupActive, exportSessions, importSessions, exportLeads, importLeads, exportDates, importDates, exportSets, importSets, exportChallenges, importChallenges, archiveBackupFolder, isCleaning ->
        ImportExportSettingState(
            exportFolder = exportFolder,
            importFolder = importFolder,
            backupFolder = backupFolder,
            exportHeader = exportHeader,
            importHeader = importHeader,
            backupActive = backupActive,
            exportSessionsFilename = exportSessions,
            importSessionsFilename = importSessions,
            exportLeadsFilename = exportLeads,
            importLeadsFilename = importLeads,
            exportDatesFilename = exportDates,
            importDatesFilename = importDates,
            exportSetsFilename = exportSets,
            importSetsFilename = importSets,
            exportChallengesFilename = exportChallenges,
            importChallengesFilename = importChallenges,
            archiveBackupFolder = archiveBackupFolder,
            isCleaning = isCleaning
        )
    }
    val _generalSettingState: Flow<GeneralSettingState> = CombineSeventeen(
        settingDao.getGenerateiDate(),
        settingDao.getNotificationTime(),
        settingDao.getFollowCount(),
        settingDao.getSuggestLeadsNationality(),
        settingDao.getShownNationalities(),
        settingDao.getAutoSetEventDateTime(),
        settingDao.getAutoSetSessionTimeToStart(),
        settingDao.getAutoSetSetTimeToStart(),
        settingDao.getAutoSetDateTimeToStart(),
        settingDao.getThemeSysFollow(),
        settingDao.getTheme(),
        settingDao.getSimplePlusOneReport(),
        settingDao.getNeverShareLeadInfo(),
        settingDao.getCopyReportOnClipboard(),
        settingDao.getShowCurrentWeekSummary(),
        settingDao.getShowCurrentMonthSummary(),
        settingDao.getShowCurrentChallengeSummary()
    ) { generateiDate, notificationTime, followCount, suggestLeadsNationality, shownNationalities, autoSetEventDateTime, autoSetSessionTimeToStart, autoSetSetTimeToStart, autoSetDateTimeToStart, themeSysFollow, themeId, simplePlusOneReport, neverShareLead, copyReportOnClipboard, showCurrentWeekSummary, showCurrentMonthSummary, showCurrentChallengeSummary ->
        GeneralSettingState(
            generateiDate = generateiDate,
            notificationTime = notificationTime,
            followCount = followCount,
            suggestLeadsNationality = suggestLeadsNationality,
            shownNationalities = shownNationalities,
            autoSetEventDateTime = autoSetEventDateTime,
            autoSetSessionTimeToStart = autoSetSessionTimeToStart,
            autoSetSetTimeToStart = autoSetSetTimeToStart,
            autoSetDateTimeToStart = autoSetDateTimeToStart,
            themeSysFollow = themeSysFollow,
            themeId = themeId,
            simplePlusOneReport = simplePlusOneReport,
            neverShareLeadInfo = neverShareLead,
            copyReportOnClipboard = copyReportOnClipboard,
            showCurrentWeekSummary = showCurrentWeekSummary,
            showCurrentMonthSummary = showCurrentMonthSummary,
            showCurrentChallengeSummary = showCurrentChallengeSummary
        )
    }
    private val _averageLast = settingDao.getAverageLast()
    private val _latestAvailable = settingDao.getLatestAvailable()
    private val _latestPublishDate = settingDao.getLatestPublishDate()
    private val _latestChangelog = settingDao.getLatestChangelog()
    private val _latestDownloadUrl = settingDao.getLatestDownloadUrl()
    private val _lastSessionsShown = settingDao.getLastSessionsShown()
    private val _lastWeeksShown = settingDao.getLastWeeksShown()
    private val _lastMonthsShown = settingDao.getLastMonthsShown()
    val state =
        CombineSixteen(
            _state,
            _allSessions,
            _allLeads,
            _allDates,
            _allSets,
            _allChallenges,
            _importExportSettingState,
            _generalSettingState,
            _averageLast,
            _latestAvailable,
            _latestPublishDate,
            _latestChangelog,
            _latestDownloadUrl,
            _lastSessionsShown,
            _lastWeeksShown,
            _lastMonthsShown
        ) { state, allSessions, allLeads, allDates, allSets, allChallenges, importExportSettingState, generalSettingState, averageLast, latestAvailable, latestPublishDate, latestChangelog, latestDownloadUrl, lastSessionsShown, lastWeeksShown, lastMonthsShown ->
            state.copy(
                exportSessionsFileName = importExportSettingState.exportSessionsFilename,
                importSessionsFileName = importExportSettingState.importSessionsFilename,
                exportLeadsFileName = importExportSettingState.exportLeadsFilename,
                importLeadsFileName = importExportSettingState.importLeadsFilename,
                exportDatesFileName = importExportSettingState.exportDatesFilename,
                importDatesFileName = importExportSettingState.importDatesFilename,
                exportSetsFileName = importExportSettingState.exportSetsFilename,
                importSetsFileName = importExportSettingState.importSetsFilename,
                exportChallengesFileName = importExportSettingState.exportChallengesFilename,
                importChallengesFileName = importExportSettingState.importChallengesFilename,
                archiveBackupFolder = importExportSettingState.archiveBackupFolder.toBoolean(),
                isCleaning = importExportSettingState.isCleaning.toBoolean(),
                themeSysFollow = generalSettingState.themeSysFollow.toBoolean(),
                theme = generalSettingState.themeId,
                exportFolder = importExportSettingState.exportFolder,
                importFolder = importExportSettingState.importFolder,
                backupFolder = importExportSettingState.backupFolder,
                notificationTime = generalSettingState.notificationTime,
                allSessions = allSessions,
                allLeads = allLeads,
                allDates = allDates,
                allSets = allSets,
                allChallenges = allChallenges,
                lastSessionAverageQuantity = averageLast,
                exportHeader = importExportSettingState.exportHeader.toBoolean(),
                importHeader = importExportSettingState.importHeader.toBoolean(),
                backupActive = importExportSettingState.backupActive.toBoolean(),
                generateiDate = generalSettingState.generateiDate.toBoolean(),
                latestAvailable = latestAvailable,
                latestPublishDate = latestPublishDate,
                latestChangelog = latestChangelog,
                latestDownloadUrl = latestDownloadUrl,
                lastSessionsShown = lastSessionsShown,
                lastWeeksShown = lastWeeksShown,
                lastMonthsShown = lastMonthsShown,
                followCount = generalSettingState.followCount.toBoolean(),
                suggestLeadsNationality = generalSettingState.suggestLeadsNationality.toBoolean(),
                shownNationalities = generalSettingState.shownNationalities.toInt(),
                autoSetEventDateTime = generalSettingState.autoSetEventDateTime.toBoolean(),
                autoSetSessionTimeToStart = generalSettingState.autoSetSessionTimeToStart.toBoolean(),
                autoSetSetTimeToStart = generalSettingState.autoSetSetTimeToStart.toBoolean(),
                autoSetDateTimeToStart = generalSettingState.autoSetDateTimeToStart.toBoolean(),
                simplePlusOneReport = generalSettingState.simplePlusOneReport.toBoolean(),
                neverShareLeadInfo = generalSettingState.neverShareLeadInfo.toBoolean(),
                copyReportOnClipboard = generalSettingState.copyReportOnClipboard.toBoolean(),
                showCurrentWeekSummary = generalSettingState.showCurrentWeekSummary.toBoolean(),
                showCurrentMonthSummary = generalSettingState.showCurrentMonthSummary.toBoolean(),
                showCurrentChallengeSummary = generalSettingState.showCurrentChallengeSummary.toBoolean()
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

            is ToolEvent.SetExportChallengesFileName -> {
                _state.update {
                    it.copy(
                        exportChallengesFileName = event.exportChallengesFileName
                    )
                }
                val exportChallengesFileName = _state.value.exportChallengesFileName
                val setting =
                    Setting(SettingDao.EXPORT_CHALLENGES_FILE_NAME_ID, exportChallengesFileName)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetImportChallengesFileName -> {
                _state.update {
                    it.copy(
                        importChallengesFileName = event.importChallengesFileName
                    )
                }
                val importChallengesFileName = _state.value.importChallengesFileName
                val setting =
                    Setting(SettingDao.IMPORT_CHALLENGES_FILE_NAME_ID, importChallengesFileName)
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

            is ToolEvent.SetAllChallenges -> {
                _state.update {
                    it.copy(
                        allChallenges = event.allChallenges
                    )
                }
                val allChallenges = _state.value.allChallenges
                viewModelScope.launch { challengeDao.batchInsert(allChallenges.map { it.challenge }) }
            }

            is ToolEvent.SetLastSessionAverageQuantity -> {
                _state.update {
                    it.copy(
                        lastSessionAverageQuantity = minMaxLimiter(
                            event.lastSessionAverageQuantity.toInt(),
                            1,
                            10
                        )
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
                        lastSessionsShown = minMaxLimiter(event.lastSessionsShown.toInt(), 1, 15)
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
                        lastWeeksShown = minMaxLimiter(event.lastWeeksShown.toInt(), 1, 12)
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
                        lastMonthsShown = minMaxLimiter(event.lastMonthsShown.toInt(), 1, 12)
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
                        lastBackup = minMaxLimiter(event.lastBackup.toInt(), 1, 10)
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

            is ToolEvent.SwitchGenerateiDate -> {
                _state.update {
                    it.copy(
                        generateiDate = _state.value.generateiDate.not()
                    )
                }
                val generateiDate = _state.value.generateiDate
                val setting = Setting(SettingDao.GENERATE_IDATE_ID, generateiDate.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchFollowCount -> {
                _state.update {
                    it.copy(
                        followCount = _state.value.followCount.not()
                    )
                }
                val followCount = _state.value.followCount
                val setting = Setting(SettingDao.FOLLOW_COUNT_ID, followCount.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchSuggestLeadsNationality -> {
                _state.update {
                    it.copy(
                        suggestLeadsNationality = _state.value.suggestLeadsNationality.not()
                    )
                }
                val suggestLeadsNationality = _state.value.suggestLeadsNationality
                val setting = Setting(
                    SettingDao.SUGGEST_LEADS_NATIONALITY_ID,
                    suggestLeadsNationality.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchAutoSetEventDateTime -> {
                _state.update {
                    it.copy(
                        autoSetEventDateTime = _state.value.autoSetEventDateTime.not()
                    )
                }
                val autoSetEventDateTime = _state.value.autoSetEventDateTime
                val setting = Setting(
                    SettingDao.AUTO_SET_EVENT_DATE_TIME_ID,
                    autoSetEventDateTime.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchAutoSetSessionTimeToStart -> {
                _state.update {
                    it.copy(
                        autoSetSessionTimeToStart = _state.value.autoSetSessionTimeToStart.not()
                    )
                }
                val autoSetSessionTimeToStart = _state.value.autoSetSessionTimeToStart
                val setting = Setting(
                    SettingDao.AUTO_SET_SESSION_TIME_TO_START_ID,
                    autoSetSessionTimeToStart.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchAutoSetSetTimeToStart -> {
                _state.update {
                    it.copy(
                        autoSetSetTimeToStart = _state.value.autoSetSetTimeToStart.not()
                    )
                }
                val autoSetSetTimeToStart = _state.value.autoSetSetTimeToStart
                val setting = Setting(
                    SettingDao.AUTO_SET_SET_TIME_TO_START_ID,
                    autoSetSetTimeToStart.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchAutoSetDateTimeToStart -> {
                _state.update {
                    it.copy(
                        autoSetDateTimeToStart = _state.value.autoSetDateTimeToStart.not()
                    )
                }
                val autoSetDateTimeToStart = _state.value.autoSetDateTimeToStart
                val setting = Setting(
                    SettingDao.AUTO_SET_DATE_TIME_TO_START_ID,
                    autoSetDateTimeToStart.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetShownNationalities -> {
                _state.update {
                    it.copy(
                        shownNationalities = minMaxLimiter(event.shownNationalities.toInt(), 1, 8)
                    )
                }
                val shownNationalities = _state.value.shownNationalities
                val setting = Setting(
                    SettingDao.SHOWN_NATIONALITIES_ID,
                    shownNationalities.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchArchiveBackupFolder -> {
                _state.update {
                    it.copy(
                        archiveBackupFolder = _state.value.archiveBackupFolder.not()
                    )
                }
                val archiveBackupFolder = _state.value.archiveBackupFolder
                val setting =
                    Setting(SettingDao.ARCHIVE_BACKUP_FOLDER_ID, archiveBackupFolder.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchDeleteSessions -> {
                _state.update {
                    it.copy(
                        deleteSessions = _state.value.deleteSessions.not()
                    )
                }
            }

            is ToolEvent.SwitchDeleteLeads -> {
                _state.update {
                    it.copy(
                        deleteLeads = _state.value.deleteLeads.not()
                    )
                }
            }

            is ToolEvent.SwitchDeleteDates -> {
                _state.update {
                    it.copy(
                        deleteDates = _state.value.deleteDates.not()
                    )
                }
            }

            is ToolEvent.SwitchDeleteChallenges -> {
                _state.update {
                    it.copy(
                        deleteChallenges = _state.value.deleteChallenges.not()
                    )
                }
            }

            is ToolEvent.SwitchDeleteSets -> {
                _state.update {
                    it.copy(
                        deleteSets = _state.value.deleteSets.not()
                    )
                }
            }

            is ToolEvent.SwitchIsCleaning -> {
                _state.update {
                    it.copy(
                        isCleaning = _state.value.isCleaning.not()
                    )
                }
                val isCleaning = _state.value.isCleaning
                val setting =
                    Setting(SettingDao.IS_CLEANING_ID, isCleaning.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchThemeSysFollow -> {
                _state.update {
                    it.copy(
                        themeSysFollow = _state.value.themeSysFollow.not()
                    )
                }
                val themeSysFollow = _state.value.themeSysFollow
                val setting =
                    Setting(SettingDao.THEME_SYS_FOLLOW_ID, themeSysFollow.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchSimplePlusOneReport -> {
                _state.update {
                    it.copy(
                        simplePlusOneReport = _state.value.simplePlusOneReport.not()
                    )
                }
                val simplePlusOneReport = _state.value.simplePlusOneReport
                val setting =
                    Setting(SettingDao.SIMPLE_PLUS_ONE_REPORT_ID, simplePlusOneReport.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchNeverShareLeadInfo -> {
                _state.update {
                    it.copy(
                        neverShareLeadInfo = _state.value.neverShareLeadInfo.not()
                    )
                }
                val neverShareLeadInfo = _state.value.neverShareLeadInfo
                val setting =
                    Setting(SettingDao.NEVER_SHARE_LEAD_INFO_ID, neverShareLeadInfo.toString())
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchCopyReportOnClipboard -> {
                _state.update {
                    it.copy(
                        copyReportOnClipboard = _state.value.copyReportOnClipboard.not()
                    )
                }
                val copyReportOnClipboard = _state.value.copyReportOnClipboard
                val setting =
                    Setting(
                        SettingDao.COPY_REPORT_ON_CLIPBOARD_ID,
                        copyReportOnClipboard.toString()
                    )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchShowCurrentWeekSummary -> {
                _state.update {
                    it.copy(
                        showCurrentWeekSummary = _state.value.showCurrentWeekSummary.not()
                    )
                }
                val showCurrentWeekSummary = _state.value.showCurrentWeekSummary
                val setting =
                    Setting(
                        SettingDao.SHOW_CURRENT_WEEK_SUMMARY_ID,
                        showCurrentWeekSummary.toString()
                    )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchShowCurrentMonthSummary -> {
                _state.update {
                    it.copy(
                        showCurrentMonthSummary = _state.value.showCurrentMonthSummary.not()
                    )
                }
                val showCurrentMonthSummary = _state.value.showCurrentMonthSummary
                val setting =
                    Setting(
                        SettingDao.SHOW_CURRENT_MONTH_SUMMARY_ID,
                        showCurrentMonthSummary.toString()
                    )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SwitchShowCurrentChallengeSummary -> {
                _state.update {
                    it.copy(
                        showCurrentChallengeSummary = _state.value.showCurrentChallengeSummary.not()
                    )
                }
                val showCurrentChallengeSummary = _state.value.showCurrentChallengeSummary
                val setting =
                    Setting(
                        SettingDao.SHOW_CURRENT_CHALLENGE_SUMMARY_ID,
                        showCurrentChallengeSummary.toString()
                    )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetIncrementChallengeGoal -> {
                _state.update {
                    it.copy(
                        incrementChallengeGoal = minMaxLimiter(
                            event.incrementChallengeGoal.toInt(),
                            1,
                            50
                        )
                    )
                }
                val incrementChallengeGoal = _state.value.incrementChallengeGoal
                val setting = Setting(
                    SettingDao.INCREMENT_CHALLENGE_GOAL_ID,
                    incrementChallengeGoal.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetDefaultChallengeGoal -> {
                _state.update {
                    it.copy(
                        defaultChallengeGoal = minMaxLimiter(
                            event.defaultChallengeGoal.toInt(),
                            1,
                            100
                        )
                    )
                }
                val defaultSetsChallengeGoal = _state.value.defaultChallengeGoal
                val setting = Setting(
                    SettingDao.DEFAULT_CHALLENGE_GOAL_ID,
                    defaultSetsChallengeGoal.toString()
                )
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetTheme -> {
                _state.update {
                    it.copy(
                        theme = event.themeId
                    )
                }
                val setting =
                    Setting(SettingDao.THEME_ID, _state.value.theme)
                viewModelScope.launch { settingDao.insert(setting) }
            }

            is ToolEvent.SetDeleteConfirmationPrompt -> {
                _state.update {
                    it.copy(
                        deleteConfirmationPrompt = event.deleteConfirmationPrompt
                    )
                }
            }

            is ToolEvent.DeleteAllSessions -> {
                viewModelScope.launch { abstractSessionDao.deleteAll() }
            }

            is ToolEvent.DeleteAllLeads -> {
                viewModelScope.launch { leadDao.deleteAll() }
            }

            is ToolEvent.DeleteAllDates -> {
                viewModelScope.launch { dateDao.deleteAll() }
            }

            is ToolEvent.DeleteAllSets -> {
                viewModelScope.launch { setDao.deleteAll() }
            }

            is ToolEvent.DeleteAllChallenges -> {
                viewModelScope.launch { challengeDao.deleteAll() }
            }
        }
    }

    fun minMaxLimiter(count: Int, min: Int, max: Int): Int {
        var newCount = count
        if (newCount < min) {
            newCount = min
        } else if (newCount > max) {
            newCount = max
        }
        return newCount
    }
}

data class ImportExportSettingState(
    val exportFolder: String,
    val importFolder: String,
    val backupFolder: String,
    val exportHeader: String,
    val importHeader: String,
    val backupActive: String,
    val exportSessionsFilename: String,
    val importSessionsFilename: String,
    val exportLeadsFilename: String,
    val importLeadsFilename: String,
    val exportDatesFilename: String,
    val importDatesFilename: String,
    val exportSetsFilename: String,
    val importSetsFilename: String,
    val exportChallengesFilename: String,
    val importChallengesFilename: String,
    val archiveBackupFolder: String,
    val isCleaning: String
)

data class GeneralSettingState(
    val generateiDate: String,
    val notificationTime: String,
    val followCount: String,
    val suggestLeadsNationality: String,
    val autoSetEventDateTime: String,
    val autoSetSessionTimeToStart: String,
    val autoSetSetTimeToStart: String,
    val autoSetDateTimeToStart: String,
    val shownNationalities: String,
    val themeSysFollow: String,
    val themeId: String,
    val simplePlusOneReport: String,
    val neverShareLeadInfo: String,
    val copyReportOnClipboard: String,
    val showCurrentWeekSummary: String,
    val showCurrentMonthSummary: String,
    val showCurrentChallengeSummary: String
)