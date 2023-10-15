package com.barryburgle.gameapp.service

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class SessionServiceTestUtils {
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SESSION_TIME: Long = 120
    val APPROACH_TIME: Long = 40
    val CONVO_RATIO = 0.667
    val REJECTION_RATIO = 0.333
    val CONTACT_RATIO = 0.333
    val INDEX = 2.65
    val DAY_OF_WEEK = DayOfWeek.WEDNESDAY
    val WEEK_NUMBER = 37
    val SETS = 3
    val CONVOS = 2
    val CONTACTS = 1
    val STICKING_POINTS = "sticking-points"
}