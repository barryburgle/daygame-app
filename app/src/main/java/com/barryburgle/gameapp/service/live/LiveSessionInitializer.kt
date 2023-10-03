package com.barryburgle.gameapp.service.live

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set
import com.barryburgle.gameapp.model.session.AbstractSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

interface LiveSessionInitializer {
    fun init(
        insertTime: Instant,
        date: LocalDate,
        startHour: LocalTime,
        endHour: LocalTime,
        setArray: Array<Set>,
        convoArray: Array<Convo>,
        contactArray: Array<Contact>,
        stickingPoints: String
    ): AbstractSession
}