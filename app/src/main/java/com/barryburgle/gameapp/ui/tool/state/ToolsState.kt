package com.barryburgle.gameapp.ui.tool.state

import com.barryburgle.gameapp.model.session.AbstractSession

data class ToolsState(
    val abstractSessionHeader: String = "",
    val abstractSessions: List<AbstractSession> = emptyList()
)
