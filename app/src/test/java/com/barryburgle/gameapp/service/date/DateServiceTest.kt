package com.barryburgle.gameapp.service.date

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.service.SessionServiceTestUtils
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class DateServiceTest : SessionServiceTestUtils() {
    val LEAD_ID: Long = 1L
    val LOCATION: String = "location"
    val COST: Int = 20
    val DATE_NUMBER: Int = 1
    val DATE_TYPE: String = "date-type"
    val PULL: Boolean = false
    val BOUNCE: Boolean = false
    val KISS: Boolean = false
    val LAY: Boolean = false
    val RECORDED: Boolean = false
    val TWEET_URL: String = "https://..."

    val dateService: DateService = DateService()

    @Test
    fun initTest() {
        val date: Date = dateService.init(
            null,
            LEAD_ID,
            LOCATION,
            DATE.toString(),
            START_HOUR.toString(),
            END_HOUR.toString(),
            COST,
            DATE_NUMBER,
            DATE_TYPE,
            PULL,
            BOUNCE,
            KISS,
            LAY,
            RECORDED,
            STICKING_POINTS,
            TWEET_URL
        )
        assertNotNull(date.insertTime)
        assertEquals(LEAD_ID, date.leadId)
        assertEquals(LOCATION, date.location)
        assertEquals(DATE.toString(), date.date!!.substring(0, 10))
        assertEquals(START_HOUR.toString(), date.startHour.substring(11, 16))
        assertEquals(END_HOUR.toString(), date.endHour.substring(11, 16))
        assertEquals(COST, date.cost)
        assertEquals(DATE_NUMBER, date.dateNumber)
        assertEquals(DATE_TYPE, date.dateType)
        assertEquals(PULL, date.pull)
        assertEquals(BOUNCE, date.bounce)
        assertEquals(KISS, date.kiss)
        assertEquals(LAY, date.lay)
        assertEquals(RECORDED, date.recorded)
        assertEquals(STICKING_POINTS, date.stickingPoints)
        assertEquals(TWEET_URL, date.tweetUrl)
        assertEquals(SESSION_TIME, date.dateTime)
        assertEquals(DAY_OF_WEEK.value, date.dayOfWeek)
        assertEquals(WEEK_NUMBER, date.weekNumber)
    }
}