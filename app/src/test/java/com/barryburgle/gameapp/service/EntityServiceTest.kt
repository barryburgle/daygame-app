package com.barryburgle.gameapp.service

import junit.framework.TestCase.assertEquals
import org.junit.Test

class EntityServiceTest : ServiceTestData() {

    @Test
    fun computeTimeTest() {
        assertEquals(SESSION_TIME, EntityService.computeTime(START_HOUR, END_HOUR))
    }

    @Test
    fun computeDayOfWeekTest() {
        assertEquals(DAY_OF_WEEK, EntityService.computeDayOfWeek(DATE))
    }

    @Test
    fun computeWeekOfYearTest() {
        assertEquals(WEEK_NUMBER, EntityService.computeWeekOfYear(DATE))
    }
}