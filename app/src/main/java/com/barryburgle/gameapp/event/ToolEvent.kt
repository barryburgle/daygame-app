package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet

sealed interface ToolEvent : GenericEvent {
    data class SetExportSessionsFileName(val exportSessionsFileName: String) : ToolEvent
    data class SetImportSessionsFileName(val importSessionsFileName: String) : ToolEvent
    data class SetExportLeadsFileName(val exportLeadsFileName: String) : ToolEvent
    data class SetImportLeadsFileName(val importLeadsFileName: String) : ToolEvent
    data class SetExportDatesFileName(val exportDatesFileName: String) : ToolEvent
    data class SetImportDatesFileName(val importDatesFileName: String) : ToolEvent
    data class SetExportSetsFileName(val exportSetsFileName: String) : ToolEvent
    data class SetImportSetsFileName(val importSetsFileName: String) : ToolEvent
    data class SetExportFolder(val exportFolder: String) : ToolEvent
    data class SetImportFolder(val importFolder: String) : ToolEvent
    data class SetBackupFolder(val backupFolder: String) : ToolEvent
    data class SetAllSessions(val allSessions: List<AbstractSession>) : ToolEvent
    data class SetAllLeads(val allLeads: List<Lead>) : ToolEvent
    data class SetAllDates(val allDates: List<Date>) : ToolEvent
    data class SetAllSets(val allSets: List<SingleSet>) : ToolEvent
    data class SetLastSessionAverageQuantity(val lastSessionAverageQuantity: String) : ToolEvent
    data class SetLastSessionsShown(val lastSessionsShown: String) : ToolEvent
    data class SetLastWeeksShown(val lastWeeksShown: String) : ToolEvent
    data class SetLastMonthsShown(val lastMonthsShown: String) : ToolEvent
    data class SetNotificationTime(val notificationTime: String) : ToolEvent
    data class SetExportHeader(val exportHeader: Boolean) : ToolEvent
    data class SetImportHeader(val importHeader: Boolean) : ToolEvent
    object SwitchBackupActive : ToolEvent
    data class SetLastBackup(val lastBackup: String) : ToolEvent
    object SwitchShowChangelog : ToolEvent
    object SwitchBackupBeforeUpdate : ToolEvent
}