package com.barryburgle.gameapp.model

import com.barryburgle.gameapp.model.session.BatchSession
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BatchSessionTest : SessionTestUtils() {
    val SETS = 3
    val CONVOS = 2
    val CONTACTS = 1


    @Test
    fun newSessionTest() {
        val batchSession = BatchSession(
            INSERT_TIME,
            DATE,
            START_HOUR,
            END_HOUR,
            SETS,
            CONVOS,
            CONTACTS,
            STICKING_POINTS
        )
        assertInitProperties(batchSession)
        assertSetsConvosContacts(batchSession)
        assertComputedProperties(batchSession)
    }

    private fun assertSetsConvosContacts(batchSession: BatchSession) {
        assertEquals(SETS, batchSession.sets)
        assertEquals(CONVOS, batchSession.convos)
        assertEquals(CONTACTS, batchSession.contacts)
    }
}