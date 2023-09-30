package com.barryburgle.gameapp.model.session

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

abstract class AbstractSession(
    insertTime: Instant,
    date: LocalDate,
    startHour: LocalTime,
    endHour: LocalTime,
    sets: Int,
    convos: Int,
    contacts: Int,
    stickingPoints: String
) {
    val insertTime = insertTime
    val date = date
    val startHour = startHour
    val endHour = endHour
    val sets = sets
    val convos = convos
    val contacts = contacts
    val stickingPoints = stickingPoints
    val sessionTime: Long
    val approachTime: Long
    val convoRatio: Double
    val rejectionRatio: Double
    val contactRatio: Double
    val index: Double
    val dayOfWeek: DayOfWeek
    val weekNumber: Int

    init {
        sessionTime = startHour.until(endHour, ChronoUnit.SECONDS)
        approachTime = sessionTime / sets
        convoRatio = (convos.toDouble() / sets.toDouble()).toDouble()
        rejectionRatio = 1 - convoRatio
        contactRatio = (contacts.toDouble() / sets.toDouble()).toDouble()
        index = computeIndex(sets, convos, contacts, sessionTime)
        dayOfWeek = date.dayOfWeek
        weekNumber = computeWeekOfYear(date)
    }


    protected fun computeIndex(sets: Int, convos: Int, contacts: Int, sessionTime: Long): Double {
        // TODO: create method for formula change
        return (sets.toDouble() * (12 * sets + 20 * convos + 30 * contacts).toDouble() / sessionTime.toDouble())
    }

    protected fun computeWeekOfYear(date: LocalDate): Int {
        val weekFields: WeekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    fun getYearAndWeek(liveSession: LiveSession, weekNumber: Int): Pair<Int, Int> {
        // TODO: method useful tot track weekly total sets + convos + contacts
        val year = liveSession.date.year
        return year to weekNumber
    }
}