package com.barryburgle.gameapp.service.date

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.service.EntityService
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateService : DateInitializer, EntityService() {

    // TODO: unit test the following method
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
        return Date(
            id,
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