package main.model

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

class Session(
    private val insertTime: Instant,
    private val date: LocalDate,
    private val startHour: LocalTime,
    private val endHour: LocalTime,
    private val sets: Array<Set>,
    private val convos: Array<Convo>,
    private val contacts: Array<Contact>,
    private val stickingPoints: String
) {
    private var sessionTime: Long = startHour.until(endHour, ChronoUnit.SECONDS)
    private var approachTime: Long = sessionTime / sets.size
    private var convoRatio: Double = (convos.size / sets.size).toDouble()
    private var rejectionRatio: Double = 1 - convoRatio
    private var contactRatio: Double = (contacts.size / sets.size).toDouble()
    private var index: Double = computeIndex()
    private var dayOfWeek: DayOfWeek = date.dayOfWeek
    private var weekNumber: Int = computeWeekOfYear()

    private fun computeIndex(): Double {
        // TODO: create method for formula change
        return (sets.size * (12 * sets.size + 20 * convos.size + 30 * contacts.size) / sessionTime).toDouble()
    }

    private fun computeWeekOfYear(): Int {
        val weekFields: WeekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }
}