package com.barryburgle.gameapp.model.session

import com.barryburgle.gameapp.model.Contact
import com.barryburgle.gameapp.model.Convo
import com.barryburgle.gameapp.model.Set

class LiveSession(
    insertTime: String,
    date: String,
    startHour: String,
    endHour: String,
    setArray: Array<Set>,
    convoArray: Array<Convo>,
    contactArray: Array<Contact>,
    stickingPoints: String,
    sessionTime: Long,
    approachTime: Long,
    convoRatio: Double,
    rejectionRatio: Double,
    contactRatio: Double,
    index: Double,
    dayOfWeek: Int,
    weekNumber: Int
) : AbstractSession(
    null,
    insertTime,
    date,
    startHour,
    endHour,
    setArray.size,
    convoArray.size,
    contactArray.size,
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