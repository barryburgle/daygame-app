package com.barryburgle.gameapp.model

import com.barryburgle.gameapp.model.session.LiveSession
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

internal class LiveSessionTest {
    val INSERT_TIME = Instant.now()
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SETS = Array(3) { Set() }
    val CONVOS = Array(2) { Convo() }
    val CONTACTS = Array(1) { Contact() }
    val STICKING_POINTS = "sticking-points"
    val SESSION_TIME: Long = 7200
    val APPROACH_TIME: Long = 2400
    val CONVO_RATIO = 0.666
    val REJECTION_RATIO = 0.334
    val CONTACT_RATIO = 0.334
    val INDEX = 0.044
    val DAY_OF_WEEK = DayOfWeek.WEDNESDAY
    val WEEK_NUMBER = 37


    @Test
    fun newSessionTest() {
        val liveSession = LiveSession(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS, CONVOS, CONTACTS, STICKING_POINTS)
        assertInitProperties(liveSession)
        assertComputedProperties(liveSession)
    }

    private fun assertInitProperties(liveSession: LiveSession) {
        assertEquals(INSERT_TIME, liveSession.insertTime)
        assertEquals(DATE, liveSession.date)
        assertEquals(START_HOUR, liveSession.startHour)
        assertEquals(END_HOUR, liveSession.endHour)
        assertEquals(SETS, liveSession.sets)
        assertEquals(CONVOS, liveSession.convos)
        assertEquals(CONTACTS, liveSession.contacts)
        assertEquals(STICKING_POINTS, liveSession.stickingPoints)
    }

    private fun assertComputedProperties(liveSession: LiveSession) {
        assertEquals(SESSION_TIME, liveSession.sessionTime)
        assertEquals(APPROACH_TIME, liveSession.approachTime)
        assertEquals((CONVO_RATIO * 100).roundToInt(), (liveSession.convoRatio * 100).roundToInt())
        assertEquals((REJECTION_RATIO * 100).roundToInt(), (liveSession.rejectionRatio * 100).roundToInt())
        assertEquals((CONTACT_RATIO * 100).roundToInt(), (liveSession.contactRatio * 100).roundToInt())
        assertEquals((INDEX * 1000).roundToInt(), (liveSession.index * 1000).roundToInt())
        assertEquals(DAY_OF_WEEK, liveSession.dayOfWeek)
        assertEquals(WEEK_NUMBER, liveSession.weekNumber)
    }
}