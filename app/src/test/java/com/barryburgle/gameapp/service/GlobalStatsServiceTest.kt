package com.barryburgle.gameapp.service

import junit.framework.TestCase
import org.junit.Test

class GlobalStatsServiceTest : ServiceTestData() {

    @Test
    fun computeSetsTest() {
        TestCase.assertEquals(
            TOTAL_SETS,
            GlobalStatsService.computeSets(abstractSessionList)
        )
    }

    @Test
    fun computeConvosTest() {
        TestCase.assertEquals(
            TOTAL_CONVOS,
            GlobalStatsService.computeConvos(abstractSessionList)
        )
    }

    @Test
    fun computeContactsTest() {
        TestCase.assertEquals(
            TOTAL_CONTACTS,
            GlobalStatsService.computeContacts(abstractSessionList)
        )
    }

    @Test
    fun computeSpentHoursTest() {
        TestCase.assertEquals(
            TOTAL_HOURS,
            GlobalStatsService.computeSessionSpentHours(abstractSessionList)
        )
    }

    @Test
    fun computeAvgApproachTimeTest() {
        TestCase.assertEquals(
            AVG_APPROACH_TIME,
            GlobalStatsService.computeAvgApproachTime(abstractSessionList)
        )
    }

    @Test
    fun computeAvgConvoRatioTest() {
        TestCase.assertEquals(
            AVG_CONVO_RATIO,
            GlobalStatsService.computeAvgConvoRatio(abstractSessionList)
        )
    }

    @Test
    fun computeAvgRejectionRatioTest() {
        TestCase.assertEquals(
            AVG_REJECTION_RATIO,
            GlobalStatsService.computeAvgRejectionRatio(abstractSessionList)
        )
    }

    @Test
    fun computeAvgContactRatioTest() {
        TestCase.assertEquals(
            AVG_CONTACT_RATIO,
            GlobalStatsService.computeAvgContactRatio(abstractSessionList)
        )
    }

    @Test
    fun computeAvgIndexTest() {
        TestCase.assertEquals(
            AVG_INDEX,
            GlobalStatsService.computeAvgIndex(abstractSessionList)
        )
    }

    @Test
    fun computeAvgLeadTime() {
        TestCase.assertEquals(
            AVG_LEAD_TIME,
            GlobalStatsService.computeAvgLeadTime(TOTAL_CONTACTS, abstractSessionList)
        )
    }
}