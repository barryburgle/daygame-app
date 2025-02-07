package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.BatchSession
import com.barryburgle.gameapp.service.AbstractSessionService
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset


class BatchSessionService : BatchSessionInitializer, AbstractSessionService() {

    override fun init(
        id: String?,
        date: String,
        startHour: String,
        endHour: String,
        sets: String,
        convos: String,
        contacts: String,
        stickingPoints: String
    ): AbstractSession {
        val id: Long? = if (id.isNullOrBlank()) null else id.toLong()
        val parsedDate = if (date.isBlank()) getLocalDateTimeNow(15, "00:00:00.000Z") else
            LocalDateTime.parse(date + DATE_SUFFIX, savingFormatter)
        val parsedStartHour =
            if (date.isBlank() || startHour.isBlank()) getLocalDateTimeNow(10, ":00.000Z") else
                LocalDateTime.parse(date + SEPARATOR + startHour + TIME_SUFFIX, savingFormatter)
        val parsedEndHour =
            if (date.isBlank() || endHour.isBlank()) getLocalDateTimeNow(10, ":00.000Z") else
                LocalDateTime.parse(date + SEPARATOR + endHour + TIME_SUFFIX, savingFormatter)
        val parsedSets: Int = if (sets.isBlank()) 0 else sets.toInt()
        val parsedConvos: Int = if (convos.isBlank()) 0 else convos.toInt()
        val parsedContacts: Int = if (contacts.isBlank()) 0 else contacts.toInt()
        val sessionTime: Long = computeSessionTime(
            parsedStartHour.toLocalTime(),
            parsedEndHour.toLocalTime()
        )
        val approachTime: Long = computeApproachTime(
            sessionTime,
            parsedSets
        )
        val convoRatio: Double = computeConvoRatio(
            parsedConvos,
            parsedSets
        )
        val rejectionRatio: Double = computeRejectionRatio(
            parsedConvos,
            parsedSets
        )
        val contactRatio: Double = computeContactRatio(
            parsedContacts,
            parsedSets
        )
        val index: Double = computeIndex(
            parsedSets,
            parsedConvos,
            parsedContacts,
            sessionTime
        )
        val dayOfWeek: DayOfWeek = computeDayOfWeek(
            parsedDate.toLocalDate()
        )
        val weekOfYear: Int = computeWeekOfYear(parsedDate.toLocalDate())
        return AbstractSession(
            id,
            OffsetDateTime.now().toString(),
            OffsetDateTime.of(parsedDate, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedStartHour, ZoneOffset.UTC).toString(),
            OffsetDateTime.of(parsedEndHour, ZoneOffset.UTC).toString(),
            parsedSets,
            parsedConvos,
            parsedContacts,
            stickingPoints,
            sessionTime,
            approachTime,
            convoRatio,
            rejectionRatio,
            contactRatio,
            index,
            dayOfWeek.value,
            weekOfYear
        )
    }

    private fun getLocalDateTimeNow(last: Int, suffix: String): LocalDateTime {
        val localDateTime = LocalDateTime.now()
        return LocalDateTime.parse(
            localDateTime.toString().dropLast(last) + suffix,
            savingFormatter
        )
    }
}