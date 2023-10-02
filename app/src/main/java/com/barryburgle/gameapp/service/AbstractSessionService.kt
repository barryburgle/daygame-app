package com.barryburgle.gameapp.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

class AbstractSessionService {
    companion object {
        val roundingMode: RoundingMode = RoundingMode.HALF_UP
        val scale: Int = 3

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
            val convoRatio = convos.toDouble() / sets.toDouble()
            return round(convoRatio)
        }

        fun computeRejectionRatio(
            convos: Int,
            sets: Int
        ): Double {
            val rejectionRatio = 1 - convos.toDouble() / sets.toDouble()
            return round(rejectionRatio)
        }

        fun computeContactRatio(
            contacts: Int,
            sets: Int
        ): Double {
            val contactRatio = contacts.toDouble() / sets.toDouble()
            return round(contactRatio)
        }

        fun computeDayOfWeek(
            date: LocalDate
        ): DayOfWeek {
            return date.dayOfWeek
        }

        fun computeIndex(sets: Int, convos: Int, contacts: Int, sessionTime: Long): Double {
            // TODO: create method for formula change
            val index =
                (sets.toDouble() * (12 * sets + 20 * convos + 30 * contacts).toDouble() / sessionTime.toDouble())
            return round(index)
        }

        fun computeWeekOfYear(date: LocalDate): Int {
            val weekFields: WeekFields = WeekFields.of(Locale.getDefault())
            return date.get(weekFields.weekOfWeekBasedYear())
        }

        fun computeMonthOfYear(date: LocalDate): Month {
            return date.month
        }

        private fun round(toRound: Double): Double {
            return BigDecimal(toRound).setScale(scale, roundingMode).toDouble()
        }
    }
}