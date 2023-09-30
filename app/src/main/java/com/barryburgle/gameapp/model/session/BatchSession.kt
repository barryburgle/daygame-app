package com.barryburgle.gameapp.model.session

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
    stickingPoints: String
) : AbstractSession(
    insertTime,
    date,
    startHour,
    endHour,
    sets,
    convos,
    contacts,
    stickingPoints
)

// TODO: do BatchSession unit tests class