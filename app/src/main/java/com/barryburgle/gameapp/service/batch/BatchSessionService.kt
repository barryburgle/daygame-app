package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.BatchSession
import com.barryburgle.gameapp.service.AbstractSessionService
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class BatchSessionService : BatchSessionInitializer, AbstractSessionService() {
    override fun init(
        insertTime: Instant,
        date: LocalDate,
        startHour: LocalTime,
        endHour: LocalTime,
        sets: Int,
        convos: Int,
        contacts: Int,
        stickingPoints: String
    ): AbstractSession {
        val sessionTime: Long = computeSessionTime(startHour, endHour)
        return BatchSession(
            insertTime, date, startHour, endHour, sets, convos, contacts, stickingPoints,
            sessionTime,
            computeApproachTime(sessionTime, sets),
            computeConvoRatio(convos, sets),
            computeRejectionRatio(convos, sets),
            computeContactRatio(contacts, sets),
            computeIndex(sets, convos, contacts, sessionTime),
            computeDayOfWeek(date),
            computeWeekOfYear(date)
        )
    }

}