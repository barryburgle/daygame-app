package com.barryburgle.gameapp.service

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class FormatService {

    companion object {
        val SAVE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

        fun getDate(
            localDate: String
        ): String {
            return DATE_FORMATTER.format(parseDate(localDate))
        }

        fun getTime(
            localTime: String
        ): String {
            return TIME_FORMATTER.format(LocalTime.parse(localTime, SAVE_FORMATTER))
        }

        fun parseDate(
            localDate: String
        ): LocalDate {
            return LocalDate.parse(localDate, SAVE_FORMATTER)
        }
    }
}