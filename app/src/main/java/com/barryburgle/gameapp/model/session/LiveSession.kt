package com.barryburgle.gameapp.model.session

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class LiveSession(
    val insertTime: Instant,
    val date: LocalDate,
    val startHour: LocalTime,
    val endHour: LocalTime,
    val sets: Array<Set>,
    val convos: Array<Convo>,
    val contacts: Array<Contact>,
    val stickingPoints: String
) : AbstractSession() {
    val sessionTime: Long = startHour.until(endHour, ChronoUnit.SECONDS)
    val approachTime: Long = sessionTime / sets.size
    val convoRatio: Double = (convos.size.toDouble() / sets.size.toDouble()).toDouble()
    val rejectionRatio: Double = 1 - convoRatio
    val contactRatio: Double = (contacts.size.toDouble() / sets.size.toDouble()).toDouble()
    val index: Double = computeIndex(sets.size, convos.size, contacts.size, sessionTime)
    val dayOfWeek: DayOfWeek = date.dayOfWeek
    val weekNumber: Int = computeWeekOfYear(date)
}