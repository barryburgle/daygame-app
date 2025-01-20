package com.barryburgle.gameapp.service

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FormatServiceTest : SessionServiceTestUtils() {
    val SAVED_DATE = "2023-10-15T10:12+00"
    val VIEW_DATE = "15-10-2023"
    val VIEW_TIME = "10:12"
    val SAVED_PERC = 0.4567
    val VIEW_PERC = "45.6 %"

    @Test
    fun getPercTest() {
        assertEquals(
            VIEW_PERC,
            FormatService.getPerc(SAVED_PERC)
        )
    }

    @Test
    fun getDateTest() {
        assertEquals(
            VIEW_DATE,
            FormatService.getDate(SAVED_DATE)
        )
    }

    @Test
    fun getTimeTest() {
        assertEquals(
            VIEW_TIME,
            FormatService.getTime(SAVED_DATE)
        )
    }
}