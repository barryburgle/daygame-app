package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.session.LiveSession
import com.barryburgle.gameapp.model.Set
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.BatchSession
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class SessionManagerTest {
    val INSERT_TIME = Instant.now()
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SETS_1 = 3
    val SETS_2 = 4
    val SETS_3 = 5
    val SETS_4 = 6
    val CONVOS_1 = 2
    val CONVOS_2 = 3
    val CONVOS_3 = 4
    val CONVOS_4 = 5
    val CONTACTS_1 = 1
    val CONTACTS_2 = 2
    val CONTACTS_3 = 3
    val CONTACTS_4 = 4
    val STICKING_POINTS = "sticking-points"
    val INDEX_LAST_3_SESSIONS: Double = 0.1654
    val INDEX_LAST_4_SESSIONS: Double = 0.1352

    @Test
    fun computeIndexMovingAverageLastThreeTest() {
        doComputeIndexMovingAverageLastNTest(3, INDEX_LAST_3_SESSIONS)
    }

    @Test
    fun computeIndexMovingAverageLastFourTest() {
        doComputeIndexMovingAverageLastNTest(4, INDEX_LAST_4_SESSIONS)
    }

    @Test
    fun computeSetsHistogramTest() {
        val actualSetsHistogram: Map<Int, Double> =
            SessionManager.computeSetsHistogram(createSessions())
        assertEquals(createExpectedSetsHistogram(), actualSetsHistogram)
    }

    @Test
    fun computeConvosHistogramTest() {
        val actualConvosHistogram: Map<Int, Double> =
            SessionManager.computeConvosHistogram(createSessions())
        assertEquals(createExpectedConvosHistogram(), actualConvosHistogram)
    }

    @Test
    fun computeContactsHistogramTest() {
        val actualContactsHistogram: Map<Int, Double> =
            SessionManager.computeContactsHistogram(createSessions())
        assertEquals(createExpectedContactsHistogram(), actualContactsHistogram)
    }

    private fun doComputeIndexMovingAverageLastNTest(window: Int, expectedIndexAverage: Double) {
        val actualIndex: Double = SessionManager.computeIndexMovingAverage(createSessions(), window)
        assertEquals(expectedIndexAverage, actualIndex, 0.0001)
    }

    private fun createSessions(): Array<AbstractSession> {
        return arrayOf(
            BatchSession(
                INSERT_TIME,
                DATE,
                START_HOUR,
                END_HOUR,
                SETS_1,
                CONVOS_1,
                CONTACTS_1,
                STICKING_POINTS
            ),
            BatchSession(
                INSERT_TIME,
                DATE,
                START_HOUR,
                END_HOUR,
                SETS_2,
                CONVOS_2,
                CONTACTS_2,
                STICKING_POINTS
            ),
            BatchSession(
                INSERT_TIME,
                DATE,
                START_HOUR,
                END_HOUR,
                SETS_3,
                CONVOS_3,
                CONTACTS_3,
                STICKING_POINTS
            ),
            BatchSession(
                INSERT_TIME,
                DATE,
                START_HOUR,
                END_HOUR,
                SETS_4,
                CONVOS_4,
                CONTACTS_4,
                STICKING_POINTS
            )
        )
    }

    private fun createExpectedSetsHistogram(): MutableMap<Int, Double> {
        val expectedSetHistogram = mutableMapOf<Int, Double>()
        expectedSetHistogram[3] = 25.0
        expectedSetHistogram[4] = 25.0
        expectedSetHistogram[5] = 25.0
        expectedSetHistogram[6] = 25.0
        return expectedSetHistogram
    }

    private fun createExpectedConvosHistogram(): MutableMap<Int, Double> {
        val expectedSetHistogram = mutableMapOf<Int, Double>()
        expectedSetHistogram[2] = 25.0
        expectedSetHistogram[3] = 25.0
        expectedSetHistogram[4] = 25.0
        expectedSetHistogram[5] = 25.0
        return expectedSetHistogram
    }

    private fun createExpectedContactsHistogram(): MutableMap<Int, Double> {
        val expectedSetHistogram = mutableMapOf<Int, Double>()
        expectedSetHistogram[1] = 25.0
        expectedSetHistogram[2] = 25.0
        expectedSetHistogram[3] = 25.0
        expectedSetHistogram[4] = 25.0
        return expectedSetHistogram
    }
}