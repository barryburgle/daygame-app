package com.barryburgle.gameapp.model.session

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class BatchSession(
    val insertTime: Instant,
    val date: LocalDate,
    val startHour: LocalTime,
    val endHour: LocalTime,
    val sets: Int,
    val convos: Int,
    val contacts: Int,
    val stickingPoints: String
) : AbstractSession() {
    val sessionTime: Long = startHour.until(endHour, ChronoUnit.SECONDS)
    val approachTime: Long = sessionTime / sets
    val convoRatio: Double = (convos.toDouble() / sets.toDouble()).toDouble()
    val rejectionRatio: Double = 1 - convoRatio
    val contactRatio: Double = (contacts.toDouble() / sets.toDouble()).toDouble()
    val index: Double = computeIndex(sets, convos, contacts, sessionTime)
    val dayOfWeek: DayOfWeek = date.dayOfWeek
    val weekNumber: Int = computeWeekOfYear(date)
}

// TODO: do BatchSession unit tests class