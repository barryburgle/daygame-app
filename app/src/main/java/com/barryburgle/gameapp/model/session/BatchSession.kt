package com.barryburgle.gameapp.model.session

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class BatchSession(
    insertTime: Instant,
    date: LocalDate,
    startHour: LocalTime,
    endHour: LocalTime,
    sets: Int,
    convos: Int,
    contacts: Int,
    stickingPoints: String,
    sessionTime: Long,
    approachTime: Long,
    convoRatio: Double,
    rejectionRatio: Double,
    contactRatio: Double,
    index: Double,
    dayOfWeek: DayOfWeek,
    weekNumber: Int
) : AbstractSession(
    insertTime,
    date,
    startHour,
    endHour,
    sets,
    convos,
    contacts,
    stickingPoints,
    sessionTime,
    approachTime,
    convoRatio,
    rejectionRatio,
    contactRatio,
    index,
    dayOfWeek,
    weekNumber
)
