package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.session.AbstractSession

data class InputState(
    val abstractSessions: List<AbstractSession> = emptyList(),
    var date: String = "",
    var startHour: String = "",
    var endHour: String = "",
    var sets: String = "",
    var convos: String = "",
    var contacts: String = "",
    var stickingPoints: String = "",
    val sortType: SortType = SortType.DATE,
    val isAddingSession: Boolean = false,
    val notificationTime: String = ""
)
