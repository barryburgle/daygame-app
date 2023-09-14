package test.manager

import main.manager.SessionManager
import main.model.Session
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class SessionManagerTest {
    val INSERT_TIME = Instant.now()
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SETS_1 = Array(3) { main.model.Set() }
    val SETS_2 = Array(4) { main.model.Set() }
    val SETS_3 = Array(5) { main.model.Set() }
    val SETS_4 = Array(6) { main.model.Set() }
    val CONVOS_1 = Array(2) { main.model.Convo() }
    val CONVOS_2 = Array(3) { main.model.Convo() }
    val CONVOS_3 = Array(4) { main.model.Convo() }
    val CONVOS_4 = Array(5) { main.model.Convo() }
    val CONTACTS_1 = Array(1) { main.model.Contact() }
    val CONTACTS_2 = Array(2) { main.model.Contact() }
    val CONTACTS_3 = Array(3) { main.model.Contact() }
    val CONTACTS_4 = Array(4) { main.model.Contact() }
    val STICKING_POINTS = "sticking-points"
    val INDEX_LAST_3_SESSIONS: Double = 0.1654
    val INDEX_LAST_4_SESSIONS: Double = 0.1352

    @Test
    fun computeIndexMovingAverageLastThreeTest() {
        doComputeIndexMovingAverageLastNTest(3, INDEX_LAST_3_SESSIONS);
    }

    @Test
    fun computeIndexMovingAverageLastFourTest() {
        doComputeIndexMovingAverageLastNTest(4, INDEX_LAST_4_SESSIONS);
    }

    private fun doComputeIndexMovingAverageLastNTest(window: Int, expectedIndexAverage: Double) {
        val actualIndex: Double = SessionManager.computeIndexMovingAverage(createSessions(), window)
        assertEquals(expectedIndexAverage, actualIndex, 0.0001)
    }

    private fun createSessions(): Array<Session> {
        return arrayOf(
            Session(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS_1, CONVOS_1, CONTACTS_1, STICKING_POINTS),
            Session(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS_2, CONVOS_2, CONTACTS_2, STICKING_POINTS),
            Session(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS_3, CONVOS_3, CONTACTS_3, STICKING_POINTS),
            Session(INSERT_TIME, DATE, START_HOUR, END_HOUR, SETS_4, CONVOS_4, CONTACTS_4, STICKING_POINTS)
        )
    }
}