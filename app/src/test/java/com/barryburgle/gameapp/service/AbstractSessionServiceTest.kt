package com.barryburgle.gameapp.service

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Month

class AbstractSessionServiceTest : SessionServiceTestUtils() {

    @Test
    fun computeSessionTimeTest() {
        assertEquals(SESSION_TIME, AbstractSessionService.computeSessionTime(START_HOUR, END_HOUR))
    }

    @Test
    fun computeApproachTimeTest() {
        assertEquals(APPROACH_TIME, AbstractSessionService.computeApproachTime(SESSION_TIME, SETS))
    }

    @Test
    fun computeRejectionRatioTest() {
        assertEquals(REJECTION_RATIO, AbstractSessionService.computeRejectionRatio(CONVOS, SETS))
    }

    @Test
    fun computeContactRatioTest() {
        assertEquals(CONTACT_RATIO, AbstractSessionService.computeContactRatio(CONTACTS, SETS))
    }

    @Test
    fun computeDayOfWeekTest() {
        assertEquals(DAY_OF_WEEK, AbstractSessionService.computeDayOfWeek(DATE))
    }

    @Test
    fun computeIndexTest() {
        assertEquals(
            INDEX,
            AbstractSessionService.computeIndex(SETS, CONVOS, CONTACTS, SESSION_TIME)
        )
    }

    @Test
    fun computeWeekOfYearTest() {
        assertEquals(WEEK_NUMBER, AbstractSessionService.computeWeekOfYear(DATE))
    }

    @Test
    fun computeMonthOfYearTest() {
        assertEquals(Month.SEPTEMBER, AbstractSessionService.computeMonthOfYear(DATE))
    }
}