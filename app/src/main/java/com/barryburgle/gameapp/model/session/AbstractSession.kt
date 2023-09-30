package com.barryburgle.gameapp.model.session

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

abstract class AbstractSession {

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