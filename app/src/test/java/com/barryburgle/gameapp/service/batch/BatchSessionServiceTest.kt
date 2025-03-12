package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.SessionServiceTestUtils
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class BatchSessionServiceTest : SessionServiceTestUtils() {

    val batchSessionService: BatchSessionService = BatchSessionService()

    @Test
    fun initTest() {
        val batchSession: AbstractSession = batchSessionService.init(
            null,
            DATE.toString(),
            START_HOUR.toString(),
            END_HOUR.toString(),
            SETS.toString(),
            CONVOS.toString(),
            CONTACTS.toString(),
            STICKING_POINTS
        )
        assertNotNull(batchSession.insertTime)
        assertEquals(DATE.toString(), batchSession.date.substring(0,10))
        assertEquals(START_HOUR.toString(), batchSession.startHour.substring(11,16))
        assertEquals(END_HOUR.toString(), batchSession.endHour.substring(11,16))
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
        assertEquals(DAY_OF_WEEK.value, batchSession.dayOfWeek)
        assertEquals(WEEK_NUMBER, batchSession.weekNumber)
    }
}
