package test.model

import main.model.Session
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

internal class SessionTest {
    val INSERT_TIME = Instant.now()
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SETS = Array(3) { main.model.Set() }
    val CONVOS = Array(2) { main.model.Convo() }
    val CONTACTS = Array(1) { main.model.Contact() }
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
        val session = Session(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS, CONVOS, CONTACTS, STICKING_POINTS)
        assertInitProperties(session)
        assertComputedProperties(session)
    }

    private fun assertInitProperties(session: Session) {
        assertEquals(INSERT_TIME, session.insertTime)
        assertEquals(DATE, session.date)
        assertEquals(START_HOUR, session.startHour)
        assertEquals(END_HOUR, session.endHour)
        assertEquals(SETS, session.sets)
        assertEquals(CONVOS, session.convos)
        assertEquals(CONTACTS, session.contacts)
        assertEquals(STICKING_POINTS, session.stickingPoints)
    }

    private fun assertComputedProperties(session: Session) {
        assertEquals(SESSION_TIME, session.sessionTime)
        assertEquals(APPROACH_TIME, session.approachTime)
        assertEquals((CONVO_RATIO * 100).roundToInt(), (session.convoRatio * 100).roundToInt())
        assertEquals((REJECTION_RATIO * 100).roundToInt(), (session.rejectionRatio * 100).roundToInt())
        assertEquals((CONTACT_RATIO * 100).roundToInt(), (session.contactRatio * 100).roundToInt())
        assertEquals((INDEX * 1000).roundToInt(), (session.index * 1000).roundToInt())
        assertEquals(DAY_OF_WEEK, session.dayOfWeek)
        assertEquals(WEEK_NUMBER, session.weekNumber)
    }
}