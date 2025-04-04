package com.barryburgle.gameapp.ui.tool.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.ui.state.OrderState

data class ToolsState(
    override var exportSessionsFileName: String = "",
    var importSessionsFileName: String = "",
    override var exportLeadsFileName: String = "",
    var importLeadsFileName: String = "",
    override var exportDatesFileName: String = "",
    var importDatesFileName: String = "",
    override var exportFolder: String = "",
    var importFolder: String = "",
    override var backupFolder: String = "",
    override var allSessions: List<AbstractSession> = emptyList(),
    override var allLeads: List<Lead> = emptyList(),
    override var allDates: List<Date> = emptyList(),
    val lastSessionAverageQuantity: Int = 4,
    val lastSessionsShown: Int = 14,
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
    var backupBeforeUpdate: Boolean = true
) : OrderState(
    null,
    exportSessionsFileName,
    exportLeadsFileName,
    exportDatesFileName,
    exportFolder,
    backupFolder,
    allSessions,
    allLeads,
    allDates,
    backupActive,
    lastBackup
)
