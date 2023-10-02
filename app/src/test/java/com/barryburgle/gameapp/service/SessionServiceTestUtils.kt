package com.barryburgle.gameapp.model

import com.barryburgle.gameapp.model.session.AbstractSession
import junit.framework.TestCase.assertEquals
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

open class SessionTestUtils {
    val INSERT_TIME = Instant.now()
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val STICKING_POINTS = "sticking-points"
    val SESSION_TIME: Long = 7200
    val APPROACH_TIME: Long = 2400
    val CONVO_RATIO = 0.666
    val REJECTION_RATIO = 0.334
    val CONTACT_RATIO = 0.334
    val INDEX = 0.044
    val DAY_OF_WEEK = DayOfWeek.WEDNESDAY
    val WEEK_NUMBER = 37


    fun assertInitProperties(abstractSession: AbstractSession) {
        assertEquals(INSERT_TIME, abstractSession.insertTime)
        assertEquals(DATE, abstractSession.date)
        assertEquals(START_HOUR, abstractSession.startHour)
        assertEquals(END_HOUR, abstractSession.endHour)
        assertEquals(STICKING_POINTS, abstractSession.stickingPoints)
    }

    fun assertComputedProperties(abstractSession: AbstractSession) {
        assertEquals(SESSION_TIME, abstractSession.sessionTime)
        assertEquals(APPROACH_TIME, abstractSession.approachTime)
        assertEquals(
            (CONVO_RATIO * 100).roundToInt(),
            (abstractSession.convoRatio * 100).roundToInt()
        )
        assertEquals(
            (REJECTION_RATIO * 100).roundToInt(),
            (abstractSession.rejectionRatio * 100).roundToInt()
        )
        assertEquals(
            (CONTACT_RATIO * 100).roundToInt(),
            (abstractSession.contactRatio * 100).roundToInt()
        )
        assertEquals(
            (INDEX * 1000).roundToInt(),
            (abstractSession.index * 1000).roundToInt()
        )
        assertEquals(DAY_OF_WEEK, abstractSession.dayOfWeek)
        assertEquals(WEEK_NUMBER, abstractSession.weekNumber)
    }
}