package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.SessionServiceTestUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BatchSessionServiceTest : SessionServiceTestUtils() {

    val batchSessionService: BatchSessionService = BatchSessionService()

    @Test
    fun initTest() {
        val batchSession: AbstractSession = batchSessionService.init(
            INSERT_TIME,
            DATE,
            START_HOUR,
            END_HOUR,
            SETS,
            CONVOS,
            CONTACTS,
            STICKING_POINTS
        )
        assertEquals(INSERT_TIME, batchSession.insertTime)
        assertEquals(DATE, batchSession.date)
        assertEquals(START_HOUR, batchSession.startHour)
        assertEquals(END_HOUR, batchSession.endHour)
        assertEquals(SETS, batchSession.sets)
        assertEquals(CONVOS, batchSession.convos)
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