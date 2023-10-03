package com.barryburgle.gameapp.service.live

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.SessionServiceTestUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test

class LiveSessionServiceTest : SessionServiceTestUtils() {

    val liveSessionService: LiveSessionService = LiveSessionService()
    val setsArrays = Array(SETS) { Set() }
    val convosArrays = Array(CONVOS) { Convo() }
    val contactsArrays = Array(CONTACTS) { Contact() }

    @Test
    fun initTest() {
        val batchSession: AbstractSession = liveSessionService.init(
            INSERT_TIME,
            DATE,
            START_HOUR,
            END_HOUR,
            setsArrays,
            convosArrays,
            contactsArrays,
            STICKING_POINTS
        )
        assertEquals(INSERT_TIME, batchSession.insertTime)
        assertEquals(DATE, batchSession.date)
        assertEquals(START_HOUR, batchSession.startHour)
        assertEquals(END_HOUR, batchSession.endHour)
        assertEquals(SETS, batchSession.sets)
        assertEquals(CONVOS, batchSession.convos)
        assertEquals(CONTACTS, batchSession.contacts)
        assertEquals(STICKING_POINTS, batchSession.stickingPoints)
        assertEquals(SESSION_TIME, batchSession.sessionTime)
        assertEquals(APPROACH_TIME, batchSession.approachTime)
        assertEquals(CONVO_RATIO, batchSession.convoRatio)
        assertEquals(REJECTION_RATIO, batchSession.rejectionRatio)
        assertEquals(CONTACT_RATIO, batchSession.contactRatio)
        assertEquals(INDEX, batchSession.index)
        assertEquals(DAY_OF_WEEK, batchSession.dayOfWeek)
        assertEquals(WEEK_NUMBER, batchSession.weekNumber)
    }
}