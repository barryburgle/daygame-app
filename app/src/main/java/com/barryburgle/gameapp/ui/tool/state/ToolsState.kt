package com.barryburgle.gameapp.ui.tool.state

import com.barryburgle.gameapp.model.session.AbstractSession

data class ToolsState(
    val exportFileName: String = "",
    val importFileName: String = "",
    val exportFolder: String = "",
    val abstractSessions: List<AbstractSession> = emptyList(),
    val lastSessionAverageQuantity: Int = 4,
    val notificationTime: String = "",
    var exportHeader: Boolean = true
)
