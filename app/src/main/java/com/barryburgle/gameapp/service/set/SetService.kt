package com.barryburgle.gameapp.service.set

import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.EntityService
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset

class SetService : SetInitializer, EntityService() {

    override fun init(
        id: String?,
        date: String,
        startHour: String,
        endHour: String,
        sessionId: Long?,
        location: String,
        conversation: Boolean,
        contact: Boolean,
        instantDate: Boolean,
        recorded: Boolean,
        leadId: Long?,
        dateId: Long?,
        stickingPoints: String,
        tweetUrl: String
    ): SingleSet {
        val parsedDate = getParsedDate(date)
        val parsedStartHour = getParsedHour(date, startHour)
        val parsedEndHour = getParsedHour(date, endHour)
        val dateTime: Long = getTime(parsedStartHour, parsedEndHour)
        val dayOfWeek: DayOfWeek = getDayOfWeek(parsedDate)
        val weekOfYear: Int = getWeekOfYear(parsedDate)
        return SingleSet(
            getId(id),
            OffsetDateTime.now().toString(),
            OffsetDateTime.of(parsedDate, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedStartHour, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedEndHour, ZoneOffset.UTC).toString(),
            sessionId,
            location,
            conversation,
            contact,
            instantDate,
            recorded,
            leadId,
            dateId,
            stickingPoints,
            tweetUrl,
            dateTime,
            dayOfWeek.value,
            weekOfYear
        )
    }
}