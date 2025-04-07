package com.barryburgle.gameapp.service

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class FormatService {

    companion object {
        val SAVE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
        val SAVE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
        val NO_DATE = "No date"

        fun getPerc(
            number: Double
        ): String {
            val inPerc: Double = (number * 1000).toInt() / 10.0
            return "${inPerc} %"
        }

        fun getDate(
            localDate: String?
        ): String {
            if(localDate==null){
                return NO_DATE
            }
            return DATE_FORMATTER.format(parseDate(localDate))
        }

        fun getTime(
            localTime: String
        ): String {
            return TIME_FORMATTER.format(LocalTime.parse(localTime, SAVE_DATE_FORMATTER))
        }

        fun parseDate(
            localDate: String
        ): LocalDate {
            return LocalDate.parse(localDate, SAVE_DATE_FORMATTER)
        }

        fun parseTime(
            localDate: String
        ): LocalTime {
            return LocalTime.parse(localDate, SAVE_TIME_FORMATTER)
        }
    }
}