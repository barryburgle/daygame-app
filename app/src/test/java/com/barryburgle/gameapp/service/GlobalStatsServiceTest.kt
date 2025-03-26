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
    fun computeDateSpentHoursTest() {
        TestCase.assertEquals(
            DATE_TOTAL_HOURS,
            GlobalStatsService.computeDateSpentHours(datesList)
        )
    }

    @Test
    fun computeSessionSpentHoursTest() {
        TestCase.assertEquals(
            TOTAL_HOURS,
            GlobalStatsService.computeSessionSpentHours(abstractSessionList)
        )
    }

    @Test
    fun computeAvgLayTimeTest() {
        TestCase.assertEquals(
            AVG_LAY_TIME,
            GlobalStatsService.computeAvgLayTime(datesList, LAYS)
        )
    }

    @Test
    fun computeAvgLayTimeZeroLaysTest() {
        TestCase.assertEquals(
            0L,
            GlobalStatsService.computeAvgLayTime(datesList, 0)
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
    fun computeAvgLeadTimeTest() {
        TestCase.assertEquals(
            AVG_LEAD_TIME,
            GlobalStatsService.computeAvgLeadTime(TOTAL_CONTACTS, abstractSessionList)
        )
    }

    @Test
    fun computeAvgLeadTimeZeroLeadsTest() {
        TestCase.assertEquals(
            0L,
            GlobalStatsService.computeAvgLeadTime(0, abstractSessionList)
        )
    }

    @Test
    fun computeGenericRatioTest() {
        TestCase.assertEquals(
            (CONTACT_RATIO * 100).toInt(),
            GlobalStatsService.computeGenericRatio(SETS, CONTACTS)
        )
    }

    @Test
    fun computeGenericRatioZeroSetsTest() {
        TestCase.assertEquals(
            0,
            GlobalStatsService.computeGenericRatio(0, CONTACTS)
        )
    }
}