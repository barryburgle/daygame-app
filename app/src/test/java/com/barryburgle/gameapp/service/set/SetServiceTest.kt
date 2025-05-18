package com.barryburgle.gameapp.service.set

import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.ServiceTestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class SetServiceTest : ServiceTestData() {

    val setService: SetService = SetService()

    @Test
    fun initTest() {
        val set: SingleSet = setService.init(
            null,
            DATE.toString(),
            START_HOUR.toString(),
            END_HOUR.toString(),
            SESSION_ID,
            LOCATION,
            CONVERSATION,
            CONTACT,
            INSTANT_DATE,
            RECORDED,
            LEAD_ID,
            DATE_ID,
            STICKING_POINTS,
            TWEET_URL
        )
        assertNotNull(set.insertTime)
        assertEquals(DATE.toString(), set.date.substring(0, 10))
        assertEquals(START_HOUR.toString(), set.startHour.substring(11, 16))
        assertEquals(END_HOUR.toString(), set.endHour.substring(11, 16))
        assertEquals(SESSION_ID, set.sessionId)
        assertEquals(LOCATION, set.location)
        assertEquals(CONVERSATION, set.conversation)
        assertEquals(CONTACT, set.contact)
        assertEquals(INSTANT_DATE, set.instantDate)
        assertEquals(RECORDED, set.recorded)
        assertEquals(LEAD_ID, set.leadId)
        assertEquals(DATE_ID, set.dateId)
        assertEquals(STICKING_POINTS, set.stickingPoints)
        assertEquals(TWEET_URL, set.tweetUrl)
        assertEquals(SESSION_TIME, set.setTime)
        assertEquals(DAY_OF_WEEK.value, set.dayOfWeek)
        assertEquals(WEEK_NUMBER, set.weekNumber)
    }
}