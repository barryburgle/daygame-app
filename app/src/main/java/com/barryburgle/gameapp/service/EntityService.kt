package com.barryburgle.gameapp.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
        fun getId(
            id: String?
        ): Long? {
            return if (id.isNullOrBlank()) null else id.toLong()
        }

        @JvmStatic
        fun getParsedDate(
            date: String
        ): LocalDateTime {
            return if (date.isBlank()) getLocalDateTimeNow(15, "00:00:00.000Z") else
                LocalDateTime.parse(date + DATE_SUFFIX, savingFormatter)
        }

        @JvmStatic
        fun getParsedHour(
            date: String,
            hour: String
        ): LocalDateTime {
            return if (date.isBlank() || hour.isBlank()) getLocalDateTimeNow(10, ":00.000Z") else
                LocalDateTime.parse(date + SEPARATOR + hour + TIME_SUFFIX, savingFormatter)
        }

        @JvmStatic
        fun getTime(
            startHour: LocalDateTime,
            endHour: LocalDateTime
        ): Long {
            return computeTime(
                startHour.toLocalTime(),
                endHour.toLocalTime()
            )
        }

        @JvmStatic
        fun getDayOfWeek(
            date: LocalDateTime
        ): DayOfWeek {
            return computeDayOfWeek(
                date.toLocalDate()
            )
        }

        @JvmStatic
        fun getWeekOfYear(
            date: LocalDateTime
        ): Int {
            return computeWeekOfYear(date.toLocalDate())
        }

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

        @JvmStatic
        protected fun getLocalDateTimeNow(last: Int, suffix: String): LocalDateTime {
            val localDateTime = LocalDateTime.now()
            return LocalDateTime.parse(
                localDateTime.toString().dropLast(last) + suffix,
                savingFormatter
            )
        }
    }
}