package com.barryburgle.gameapp.model.session

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class LiveSession(
    insertTime: Instant,
    date: LocalDate,
    startHour: LocalTime,
    endHour: LocalTime,
    val setArray: Array<Set>,
    val convoArray: Array<Convo>,
    val contactArray: Array<Contact>,
    stickingPoints: String
) : AbstractSession(
    insertTime,
    date,
    startHour,
    endHour,
    setArray.size,
    convoArray.size,
    contactArray.size,
    stickingPoints
)