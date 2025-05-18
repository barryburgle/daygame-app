package com.barryburgle.gameapp.service.set

import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.EntityService
import java.time.DayOfWeek
import java.time.LocalDateTime
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
        // TODO: Group the following under some methods common to all EntityService children
        val id: Long? = if (id.isNullOrBlank()) null else id.toLong()
        val parsedDate = if (date.isBlank()) getLocalDateTimeNow(15, "00:00:00.000Z") else
            LocalDateTime.parse(date + DATE_SUFFIX, savingFormatter)
        val parsedStartHour =
            if (date.isBlank() || startHour.isBlank()) getLocalDateTimeNow(10, ":00.000Z") else
                LocalDateTime.parse(date + SEPARATOR + startHour + TIME_SUFFIX, savingFormatter)
        val parsedEndHour =
            if (date.isBlank() || endHour.isBlank()) getLocalDateTimeNow(10, ":00.000Z") else
                LocalDateTime.parse(date + SEPARATOR + endHour + TIME_SUFFIX, savingFormatter)
        val dateTime: Long = computeTime(
            parsedStartHour.toLocalTime(),
            parsedEndHour.toLocalTime()
        )
        val dayOfWeek: DayOfWeek = computeDayOfWeek(
            parsedDate.toLocalDate()
        )
        val weekOfYear: Int = computeWeekOfYear(parsedDate.toLocalDate())
        return SingleSet(
            id,
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