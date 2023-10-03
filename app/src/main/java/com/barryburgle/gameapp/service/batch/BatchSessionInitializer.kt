package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

interface BatchSessionInitializer {
    fun init(
        insertTime: Instant,
        date: LocalDate,
        startHour: LocalTime,
        endHour: LocalTime,
        sets: Int,
        convos: Int,
        contacts: Int,
        stickingPoints: String
    ): AbstractSession
}