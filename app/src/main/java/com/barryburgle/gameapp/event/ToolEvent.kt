package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession

sealed interface ToolEvent {
    data class SetExportSessionsFileName(val exportSessionsFileName: String) : ToolEvent
    data class SetImportSessionsFileName(val importSessionsFileName: String) : ToolEvent
    data class SetExportLeadsFileName(val exportLeadsFileName: String) : ToolEvent
    data class SetImportLeadsFileName(val importLeadsFileName: String) : ToolEvent
    data class SetExportFolder(val exportFolder: String) : ToolEvent
    data class SetImportFolder(val importFolder: String) : ToolEvent
    data class SetAbstractSessions(val abstractSessions: List<AbstractSession>) : ToolEvent
    data class SetLeads(val leads: List<Lead>) : ToolEvent
    data class SetLastSessionAverageQuantity(val lastSessionAverageQuantity: String) : ToolEvent
    data class SetNotificationTime(val notificationTime: String) : ToolEvent
    data class SetExportHeader(val exportHeader: Boolean) : ToolEvent
    data class SetImportHeader(val importHeader: Boolean) : ToolEvent
    object SwitchShowChangelog : ToolEvent
}