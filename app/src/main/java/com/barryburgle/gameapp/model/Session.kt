package com.barryburgle.gameapp.model

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

class Session(
    val insertTime: Instant,
    val date: LocalDate,
    val startHour: LocalTime,
    val endHour: LocalTime,
    val sets: Array<Set>,
    val convos: Array<Convo>,
    val contacts: Array<Contact>,
    val stickingPoints: String
) {
    val sessionTime: Long = startHour.until(endHour, ChronoUnit.SECONDS)
    val approachTime: Long = sessionTime / sets.size
    val convoRatio: Double = (convos.size.toDouble() / sets.size.toDouble()).toDouble()
    val rejectionRatio: Double = 1 - convoRatio
    val contactRatio: Double = (contacts.size.toDouble() / sets.size.toDouble()).toDouble()
    val index: Double = computeIndex()
    val dayOfWeek: DayOfWeek = date.dayOfWeek
    val weekNumber: Int = computeWeekOfYear()

    private fun computeIndex(): Double {
        // TODO: create method for formula change
        return (sets.size.toDouble() * (12 * sets.size + 20 * convos.size + 30 * contacts.size).toDouble() / sessionTime.toDouble())
    }

    private fun computeWeekOfYear(): Int {
        val weekFields: WeekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    fun getYearAndWeek(session: Session): Pair<Int, Int> {
        // TODO: method useful tot track weekly total sets + convos + contacts
        val year = session.date.year
        return year to weekNumber
    }
}