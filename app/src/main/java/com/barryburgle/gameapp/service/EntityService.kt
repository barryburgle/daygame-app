package com.barryburgle.gameapp.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

open class EntityService {
    companion object {
        val DATE_SUFFIX = "T00:00:00.000+00"
        val TIME_SUFFIX = ":00.000+00"
        val SEPARATOR = "T"
        val savingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val roundingMode: RoundingMode = RoundingMode.HALF_UP
        val scale: Int = 3

        @JvmStatic
        fun computeTime(
            startHour: LocalTime,
            endHour: LocalTime
        ): Long {
            return startHour.until(endHour, ChronoUnit.MINUTES)
        }

        @JvmStatic
        fun computeDayOfWeek(
            date: LocalDate
        ): DayOfWeek {
            return date.dayOfWeek
        }

        @JvmStatic
        fun computeWeekOfYear(date: LocalDate): Int {
            val weekFields: WeekFields = WeekFields.of(Locale.getDefault())
            return date.get(weekFields.weekOfWeekBasedYear())
        }

        @JvmStatic
        protected fun round(toRound: Double): Double {
            return BigDecimal(toRound).setScale(scale, roundingMode).toDouble()
        }
    }
}