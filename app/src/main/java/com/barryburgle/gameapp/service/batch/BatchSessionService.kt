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
        date: String,
        startHour: String,
        endHour: String,
        sets: String,
        convos: String,
        contacts: String,
        stickingPoints: String
    ): AbstractSession {
        val parsedDate =
            LocalDateTime.parse(date + DATE_SUFFIX, savingFormatter)
        val parsedStartHour =
            LocalDateTime.parse(date + SEPARATOR + startHour + TIME_SUFFIX, savingFormatter)
        val parsedEndHour =
            LocalDateTime.parse(date + SEPARATOR + endHour + TIME_SUFFIX, savingFormatter)
        val parsedSets: Int = sets.toInt()
        val parsedConvos: Int = convos.toInt()
        val parsedContacts: Int = contacts.toInt()
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
        return BatchSession(
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
}