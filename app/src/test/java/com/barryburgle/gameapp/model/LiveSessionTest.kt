package com.barryburgle.gameapp.model

import com.barryburgle.gameapp.model.session.LiveSession
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class LiveSessionTest : SessionTestUtils() {
    val SETS = Array(3) { Set() }
    val CONVOS = Array(2) { Convo() }
    val CONTACTS = Array(1) { Contact() }


    @Test
    fun newSessionTest() {
        val liveSession = LiveSession(
            INSERT_TIME,
            DATE,
            START_HOUR,
            END_HOUR,
            SETS,
            CONVOS,
            CONTACTS,
            STICKING_POINTS
        )
        assertInitProperties(liveSession)
        assertSetsConvosContacts(liveSession)
        assertComputedProperties(liveSession)
    }

    private fun assertSetsConvosContacts(liveSession: LiveSession) {
        assertEquals(SETS.size, liveSession.sets)
        assertEquals(SETS, liveSession.setArray)
        assertEquals(CONVOS.size, liveSession.convos)
        assertEquals(CONVOS, liveSession.convoArray)
        assertEquals(CONTACTS.size, liveSession.contacts)
        assertEquals(CONTACTS, liveSession.contactArray)
    }
}