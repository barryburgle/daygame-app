package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.batch.BatchSessionService
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class SessionManagerTest {
    val batchSessionService = BatchSessionService()
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
    val INDEX_LAST_3_SESSIONS: Double = 9.9277
    val INDEX_LAST_4_SESSIONS: Double = 8.1083

    @Test
    fun normalizeSessionsIdsTest() {
        val normalizedSessionList: List<AbstractSession> =
            SessionManager.normalizeSessionsIds(createSessionList())
        assertEquals(0L, normalizedSessionList.get(0).id)
        assertEquals(1L, normalizedSessionList.get(1).id)
    }

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
        assertEquals(expectedIndexAverage, actualIndex, 0.001)
    }

    private fun createSessions(): Array<AbstractSession> {
        return arrayOf(
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_1.toString(),
                CONVOS_1.toString(),
                CONTACTS_1.toString(),
                STICKING_POINTS
            ),
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_2.toString(),
                CONVOS_2.toString(),
                CONTACTS_2.toString(),
                STICKING_POINTS
            ),
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_3.toString(),
                CONVOS_3.toString(),
                CONTACTS_3.toString(),
                STICKING_POINTS
            ),
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_4.toString(),
                CONVOS_4.toString(),
                CONTACTS_4.toString(),
                STICKING_POINTS
            )
        )
    }

    private fun createSessionList(): List<AbstractSession> {
        return listOf(
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_1.toString(),
                CONVOS_1.toString(),
                CONTACTS_1.toString(),
                STICKING_POINTS
            ),
            batchSessionService.init(
                DATE.toString(),
                START_HOUR.toString(),
                END_HOUR.toString(),
                SETS_2.toString(),
                CONVOS_2.toString(),
                CONTACTS_2.toString(),
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
