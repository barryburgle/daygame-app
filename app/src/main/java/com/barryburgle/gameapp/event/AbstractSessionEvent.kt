package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.enums.SortType
import com.barryburgle.gameapp.model.session.AbstractSession
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

sealed interface AbstractSessionEvent {
    object SaveAbstractSession : AbstractSessionEvent
    object ShowDialog : AbstractSessionEvent
    object HideDialog : AbstractSessionEvent
    data class SetInsertTime(val insertTime: Instant) : AbstractSessionEvent
    data class SetDate(val date: LocalDate) : AbstractSessionEvent
    data class SetStartHour(val startHour: LocalTime) : AbstractSessionEvent
    data class SetEndHour(val endHour: LocalTime) : AbstractSessionEvent
    data class SetSets(val sets: Int) : AbstractSessionEvent
    data class SetConvos(val convos: Int) : AbstractSessionEvent
    data class SetContacts(val contacts: Int) : AbstractSessionEvent
    data class SetStickingPoints(val stickingPoints: String) : AbstractSessionEvent
    data class SetSessionTime(val sessionTime: Long) : AbstractSessionEvent
    data class SetApproachTime(val approachTime: Long) : AbstractSessionEvent
    data class SetConvoRatio(val convoRatio: Double) : AbstractSessionEvent
    data class SetRejectionRatio(val rejectionRatio: Double) : AbstractSessionEvent
    data class SetContactRatio(val contactRatio: Double) : AbstractSessionEvent
    data class SetIndex(val index: Double) : AbstractSessionEvent
    data class SetDayOfWeek(val dayOfWeek: DayOfWeek) : AbstractSessionEvent
    data class SetWeekNumber(val weekNumber: Int) : AbstractSessionEvent
    data class SortSessions(val sortType: SortType) : AbstractSessionEvent
    data class DeleteSession(val abstractSession: AbstractSession) : AbstractSessionEvent
}