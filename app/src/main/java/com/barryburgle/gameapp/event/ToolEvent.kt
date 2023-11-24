package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.session.AbstractSession

sealed interface ToolEvent {
    object SaveExportFileName : ToolEvent
    object SaveImportFileName : ToolEvent
    object SaveExportFolder : ToolEvent
    object SaveLastSessionAverageQuantity : ToolEvent
    object SaveNotificationTime : ToolEvent
    data class SetExportFileName(val exportFileName: String) : ToolEvent
    data class SetImportFileName(val importFileName: String) : ToolEvent
    data class SetExportFolder(val exportFolder: String) : ToolEvent
    data class SetAbstractSessions(val abstractSessions: List<AbstractSession>) : ToolEvent
    data class SetLastSessionAverageQuantity(val lastSessionAverageQuantity: String) : ToolEvent
    data class SetNotificationTime(val notificationTime: String) : ToolEvent
}