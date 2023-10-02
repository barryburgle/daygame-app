package com.barryburgle.gameapp.service

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

class AbstractSessionService {
    companion object {
        fun computeSessionTime(
            startHour: LocalTime,
            endHour: LocalTime
        ): Long {
            return startHour.until(endHour, ChronoUnit.SECONDS)
        }

        fun computeApproachTime(
            sessionTime: Long,
            sets: Int
        ): Long {
            return sessionTime / sets
        }

        fun computeConvoRatio(
            convos: Int,
            sets: Int
        ): Double {
            return convos.toDouble() / sets.toDouble()
        }

        fun computeRejectionRatio(
            convos: Int,
            sets: Int
        ): Double {
            return 1 - convos.toDouble() / sets.toDouble()
        }

        fun computeContactRatio(
            contacts: Int,
            sets: Int
        ): Double {
            return contacts.toDouble() / sets.toDouble()
        }

        fun computeDayOfWeek(
            date: LocalDate
        ): DayOfWeek {
            return date.dayOfWeek
        }

        fun computeIndex(sets: Int, convos: Int, contacts: Int, sessionTime: Long): Double {
            // TODO: create method for formula change
            return (sets.toDouble() * (12 * sets + 20 * convos + 30 * contacts).toDouble() / sessionTime.toDouble())
        }

        fun computeWeekOfYear(date: LocalDate): Int {
            val weekFields: WeekFields = WeekFields.of(Locale.getDefault())
            return date.get(weekFields.weekOfWeekBasedYear())
        }

        fun computeMonthOfYear(date: LocalDate): Month {
            return date.month
        }
    }
}