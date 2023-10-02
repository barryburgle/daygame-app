package com.barryburgle.gameapp.service

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.LiveSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class LiveSessionService : BatchSessionInitializer, AbstractSessionService() {
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
        val LiveSession = LiveSession(insertTime,date,startHour,endHour,)
    }
}