package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.session.AbstractSession

sealed interface AbstractSessionEvent {
    object SaveAbstractSession : AbstractSessionEvent
    object ShowDialog : AbstractSessionEvent
    object HideDialog : AbstractSessionEvent
    data class SetDate(val date: String) : AbstractSessionEvent
    data class SetStartHour(val startHour: String) : AbstractSessionEvent
    data class SetEndHour(val endHour: String) : AbstractSessionEvent
    data class SetSets(val sets: String) : AbstractSessionEvent
    data class SetConvos(val convos: String) : AbstractSessionEvent
    data class SetContacts(val contacts: String) : AbstractSessionEvent
    data class SetStickingPoints(val stickingPoints: String) : AbstractSessionEvent
    data class SortSessions(val sortType: SortType) : AbstractSessionEvent
    data class DeleteSession(val abstractSession: AbstractSession) : AbstractSessionEvent
}