package com.barryburgle.gameapp.service.date

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.service.EntityService
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateService : DateInitializer, EntityService() {

    override fun init(
        id: String?,
        leadId: Long,
        location: String,
        date: String,
        startHour: String,
        endHour: String,
        cost: Int?,
        dateNumber: Int,
        dateType: String,
        pull: Boolean,
        bounce: Boolean,
        kiss: Boolean,
        lay: Boolean,
        recorded: Boolean,
        stickingPoints: String,
        tweetUrl: String
    ): Date {
        val parsedDate = getParsedDate(date)
        val parsedStartHour = getParsedHour(date, startHour)
        val parsedEndHour = getParsedHour(date, endHour)
        val dateTime: Long = getTime(parsedStartHour, parsedEndHour)
        val dayOfWeek: DayOfWeek = getDayOfWeek(parsedDate)
        val weekOfYear: Int = getWeekOfYear(parsedDate)
        return Date(
            getId(id),
            OffsetDateTime.now().toString(),
            leadId,
            location,
            OffsetDateTime.of(parsedDate, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedStartHour, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedEndHour, ZoneOffset.UTC).toString(),
            cost,
            dateNumber,
            dateType,
            pull,
            bounce,
            kiss,
            lay,
            recorded,
            stickingPoints,
            tweetUrl,
            dateTime,
            dayOfWeek.value,
            weekOfYear
        )
    }
}