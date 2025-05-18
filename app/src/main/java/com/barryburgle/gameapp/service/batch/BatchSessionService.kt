package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.AbstractSessionService
import java.time.DayOfWeek
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
        val parsedDate = getParsedDate(date)
        val parsedStartHour = getParsedHour(date, startHour)
        val parsedEndHour = getParsedHour(date, endHour)
        val parsedSets: Int = if (sets.isBlank()) 0 else sets.toInt()
        val parsedConvos: Int = if (convos.isBlank()) 0 else convos.toInt()
        val parsedContacts: Int = if (contacts.isBlank()) 0 else contacts.toInt()
        val sessionTime: Long = getTime(parsedStartHour, parsedEndHour)
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
        val dayOfWeek: DayOfWeek = getDayOfWeek(parsedDate)
        val weekOfYear: Int = getWeekOfYear(parsedDate)
        return AbstractSession(
            getId(id),
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