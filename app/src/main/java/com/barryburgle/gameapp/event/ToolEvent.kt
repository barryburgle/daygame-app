package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.session.AbstractSession

sealed interface ToolEvent {
    data class SetExportFileName(val exportFileName: String) : ToolEvent
    data class SetImportFileName(val importFileName: String) : ToolEvent
    data class SetExportFolder(val exportFolder: String) : ToolEvent
    data class SetAbstractSessions(val abstractSessions: List<AbstractSession>) : ToolEvent
}