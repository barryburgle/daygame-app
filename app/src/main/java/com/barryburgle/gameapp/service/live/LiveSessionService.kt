package com.barryburgle.gameapp.service.live

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.LiveSession
import com.barryburgle.gameapp.service.AbstractSessionService
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class LiveSessionService : LiveSessionInitializer, AbstractSessionService() {
    override fun init(
        insertTime: Instant,
        date: LocalDate,
        startHour: LocalTime,
        endHour: LocalTime,
        sets: Array<Set>,
        convos: Array<Convo>,
        contacts: Array<Contact>,
        stickingPoints: String
    ): AbstractSession {
        val sessionTime: Long = computeSessionTime(startHour, endHour)
        return LiveSession(
            insertTime, date, startHour, endHour, sets, convos, contacts, stickingPoints,
            sessionTime,
            computeApproachTime(sessionTime, sets.size),
            computeConvoRatio(convos.size, sets.size),
            computeRejectionRatio(convos.size, sets.size),
            computeContactRatio(contacts.size, sets.size),
            computeIndex(sets.size, convos.size, contacts.size, sessionTime),
            computeDayOfWeek(date),
            computeWeekOfYear(date)
        )
    }
}