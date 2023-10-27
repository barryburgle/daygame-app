package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.github.mikephil.charting.data.BarEntry
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class SessionManagerTest {
    val batchSessionService = BatchSessionService()
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
    val EXPECTED_MOVING_AVERAGE_3: List<BarEntry> = listOf(
        BarEntry(0f, 1f),
        BarEntry(1f, 1.5f),
        BarEntry(2f, 2f),
        BarEntry(3f, 3f),
    )
    val EXPECTED_MOVING_AVERAGE_4: List<BarEntry> = listOf(
        BarEntry(0f, 1f),
        BarEntry(1f, 1.5f),
        BarEntry(2f, 2f),
        BarEntry(3f, 2.5f),
    )

    @Test
    fun normalizeSessionsIdsTest() {
        val normalizedSessionList: List<AbstractSession> =
            SessionManager.normalizeSessionsIds(createSessionList())
        assertEquals(0L, normalizedSessionList.get(0).id)
        assertEquals(1L, normalizedSessionList.get(1).id)
    }

    @Test
    fun computeAverageBarEntryListTest() {
        val barEntryList: List<BarEntry> =
            listOf(BarEntry(0f, 0f), BarEntry(0f, 1f), BarEntry(0f, 2f))
        val averageBarEntryList: List<BarEntry> =
            SessionManager.computeAverageBarEntryList(barEntryList)
        for (averageBarEntry in averageBarEntryList) {
            assertEquals(1f, averageBarEntry.y)
        }
    }

    @Test
    fun computeIndexMovingAverageLastThreeTest() {
        doComputeMovingAverageLastNTest(3, EXPECTED_MOVING_AVERAGE_3)
    }

    @Test
    fun computeIndexMovingAverageLastFourTest() {
        doComputeMovingAverageLastNTest(4, EXPECTED_MOVING_AVERAGE_4)
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

    private fun doComputeMovingAverageLastNTest(
        window: Int,
        expectedMovingAverage: List<BarEntry>
    ) {
        val movingAverageBarEntryList: List<BarEntry> =
            SessionManager.computeMovingAverage(createBarEntryList(), window)
        for (index in movingAverageBarEntryList.indices) {
            assertEquals(expectedMovingAverage.get(index).x, movingAverageBarEntryList.get(index).x)
            assertEquals(expectedMovingAverage.get(index).y, movingAverageBarEntryList.get(index).y)
        }
    }

    private fun createBarEntryList(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 1f),
            BarEntry(0f, 2f),
            BarEntry(0f, 3f),
            BarEntry(0f, 4f),
        )
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
