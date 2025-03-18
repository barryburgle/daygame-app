package com.barryburgle.gameapp.service

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.session.AbstractSession
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class ServiceTestData {
    val DATE = LocalDate.of(2023, 9, 13)
    val START_HOUR = LocalTime.of(16, 0, 0)
    val END_HOUR = LocalTime.of(18, 0, 0)
    val SESSION_TIME: Long = 120
    val APPROACH_TIME: Long = 40
    val CONVO_RATIO = 0.667
    val REJECTION_RATIO = 0.333
    val CONTACT_RATIO = 0.333
    val INDEX = 2.65
    val DAY_OF_WEEK = DayOfWeek.WEDNESDAY
    val WEEK_NUMBER = 37
    val SETS = 3
    val CONVOS = 2
    val CONTACTS = 1
    val STICKING_POINTS = "sticking-points"

    val SECOND_DATE = LocalDate.of(2023, 9, 14)
    val SECOND_START_HOUR = LocalTime.of(16, 0, 0)
    val SECOND_END_HOUR = LocalTime.of(19, 0, 0)
    val SECOND_SESSION_TIME: Long = 180
    val SECOND_APPROACH_TIME: Long = 30
    val SECOND_CONVO_RATIO = 0.833
    val SECOND_REJECTION_RATIO = 0.166
    val SECOND_CONTACT_RATIO = 0.6
    val SECOND_INDEX = 8.73
    val SECOND_DAY_OF_WEEK = DayOfWeek.THURSDAY
    val SECOND_WEEK_NUMBER = 37
    val SECOND_SETS = 6
    val SECOND_CONVOS = 5
    val SECOND_CONTACTS = 3
    val SECOND_STICKING_POINTS = "sticking-points"

    val TOTAL_SETS = 9
    val TOTAL_CONVOS = 7
    val TOTAL_CONTACTS = 4
    val TOTAL_HOURS = 5L
    val AVG_APPROACH_TIME = 33L
    val AVG_CONVO_RATIO = 75
    val AVG_REJECTION_RATIO = 24
    val AVG_CONTACT_RATIO = 46
    val AVG_INDEX = 5.69
    val AVG_LEAD_TIME = 75L

    val LEAD_ID: Long = 1L
    val LOCATION: String = "location"
    val COST: Int = 20
    val DATE_NUMBER: Int = 1
    val DATE_TYPE: String = "date-type"
    val PULL: Boolean = false
    val BOUNCE: Boolean = false
    val KISS: Boolean = false
    val LAY: Boolean = true
    val RECORDED: Boolean = false
    val TWEET_URL: String = "https://..."
    val AVG_LAY_TIME = 120L
    val DATE_TOTAL_HOURS = 4L
    val LAYS = 2

    val abstractSessionList: List<AbstractSession> = listOf(
        AbstractSession(
            null,
            DATE.toString(),
            DATE.toString(),
            START_HOUR.toString(),
            END_HOUR.toString(),
            SETS,
            CONVOS,
            CONTACTS,
            STICKING_POINTS,
            SESSION_TIME,
            APPROACH_TIME,
            CONVO_RATIO,
            REJECTION_RATIO,
            CONTACT_RATIO,
            INDEX,
            DAY_OF_WEEK.value,
            WEEK_NUMBER
        ),
        AbstractSession(
            null,
            SECOND_DATE.toString(),
            SECOND_DATE.toString(),
            SECOND_START_HOUR.toString(),
            SECOND_END_HOUR.toString(),
            SECOND_SETS,
            SECOND_CONVOS,
            SECOND_CONTACTS,
            SECOND_STICKING_POINTS,
            SECOND_SESSION_TIME,
            SECOND_APPROACH_TIME,
            SECOND_CONVO_RATIO,
            SECOND_REJECTION_RATIO,
            SECOND_CONTACT_RATIO,
            SECOND_INDEX,
            SECOND_DAY_OF_WEEK.value,
            SECOND_WEEK_NUMBER
        )
    )

    val datesList: List<Date> = listOf(
        Date(
            null,
            DATE.toString(),
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
            TWEET_URL,
            SESSION_TIME,
            DAY_OF_WEEK.value,
            WEEK_NUMBER
        ),
        Date(
            null,
            DATE.toString(),
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
            TWEET_URL,
            SESSION_TIME,
            DAY_OF_WEEK.value,
            WEEK_NUMBER
        )
    )
}