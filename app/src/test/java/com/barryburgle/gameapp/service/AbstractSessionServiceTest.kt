package com.barryburgle.gameapp.service

import junit.framework.TestCase.assertEquals
import org.junit.Test

class AbstractSessionServiceTest : SessionServiceTestUtils() {

    @Test
    fun computeApproachTimeTest() {
        assertEquals(APPROACH_TIME, AbstractSessionService.computeApproachTime(SESSION_TIME, SETS))
    }

    @Test
    fun computeApproachTimeNoSetsTest() {
        assertEquals(0, AbstractSessionService.computeApproachTime(SESSION_TIME, 0))
    }

    @Test
    fun computeConvoRatioTest() {
        assertEquals(CONVO_RATIO, AbstractSessionService.computeConvoRatio(CONVOS, SETS))
    }

    @Test
    fun computeConvoRatioNoSetsTest() {
        assertEquals(0.0, AbstractSessionService.computeConvoRatio(CONVOS, 0))
    }

    @Test
    fun computeRejectionRatioTest() {
        assertEquals(REJECTION_RATIO, AbstractSessionService.computeRejectionRatio(CONVOS, SETS))
    }

    @Test
    fun computeRejectionRatioNoSetsTest() {
        assertEquals(1.0, AbstractSessionService.computeRejectionRatio(CONVOS, 0))
    }

    @Test
    fun computeContactRatioTest() {
        assertEquals(CONTACT_RATIO, AbstractSessionService.computeContactRatio(CONTACTS, SETS))
    }

    @Test
    fun computeContactRatioNoSetsTest() {
        assertEquals(0.0, AbstractSessionService.computeContactRatio(CONTACTS, 0))
    }

    @Test
    fun computeIndexTest() {
        assertEquals(
            INDEX,
            AbstractSessionService.computeIndex(SETS, CONVOS, CONTACTS, SESSION_TIME)
        )
    }
}