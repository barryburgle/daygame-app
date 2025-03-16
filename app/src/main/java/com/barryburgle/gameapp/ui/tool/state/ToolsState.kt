package com.barryburgle.gameapp.ui.tool.state

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession

data class ToolsState(
    val exportSessionsFileName: String = "",
    val importSessionsFileName: String = "",
    val exportLeadsFileName: String = "",
    val importLeadsFileName: String = "",
    val exportDatesFileName: String = "",
    val importDatesFileName: String = "",
    val exportFolder: String = "",
    val importFolder: String = "",
    val abstractSessions: List<AbstractSession> = emptyList(),
    val leads: List<Lead> = emptyList(),
    val dates: List<Date> = emptyList(),
    val lastSessionAverageQuantity: Int = 4,
    val notificationTime: String = "",
    var exportHeader: Boolean = true,
    var importHeader: Boolean = true,
    val latestAvailable: String = "",
    val latestPublishDate: String = "",
    val latestChangelog: String = "",
    val latestDownloadUrl: String = "",
    val showChangelog: Boolean = false,
    val exportAll: Boolean = true
)
