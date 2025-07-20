package com.barryburgle.gameapp.ui.tool.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.ui.state.ExportState

data class ToolsState(
    override var exportSessionsFileName: String = "",
    var importSessionsFileName: String = "",
    override var exportLeadsFileName: String = "",
    var importLeadsFileName: String = "",
    override var exportDatesFileName: String = "",
    var importDatesFileName: String = "",
    override var exportSetsFileName: String = "",
    var importSetsFileName: String = "",
    override var exportFolder: String = "",
    var importFolder: String = "",
    override var backupFolder: String = "",
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    override var allSets: List<SingleSet> = emptyList(),
    val lastSessionAverageQuantity: Int = 4,
    val lastSessionsShown: Int = 14,
    val lastWeeksShown: Int = 8,
    val lastMonthsShown: Int = 4,
    val notificationTime: String = "",
    var exportHeader: Boolean = true,
    var importHeader: Boolean = true,
    override var backupActive: Boolean = true,
    override var lastBackup: Int = 3,
    val latestAvailable: String = "",
    val latestPublishDate: String = "",
    val latestChangelog: String = "",
    val latestDownloadUrl: String = "",
    val showChangelog: Boolean = false,
    var backupBeforeUpdate: Boolean = true,
    override var generateiDate: Boolean = true,
    var followCount: Boolean = false,
    var archiveBackupFolder: Boolean = true,
    var deleteSessions: Boolean = false,
    var deleteLeads: Boolean = false,
    var deleteDates: Boolean = false,
    var deleteSets: Boolean = false,
    var isCleaning: Boolean = false,
    var themeSysFollow: Boolean = true,
    var simplePlusOneReport: Boolean = true,
    var neverShareLeadInfo: Boolean = false,
    var theme: String = ThemeEnum.LIGHT.type,
    var deleteConfirmationPrompt: String = ""
) : ExportState(
    null,
    exportSessionsFileName,
    exportLeadsFileName,
    exportDatesFileName,
    exportSetsFileName,
    exportFolder,
    backupFolder,
    allSessions,
    allLeads,
    allDates,
    allSets,
    backupActive,
    lastBackup
)
