package com.barryburgle.gameapp.model.session

class BatchSession(
    insertTime: String,
    date: String,
    startHour: String,
    endHour: String,
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
    dayOfWeek: Int,
    weekNumber: Int
) : AbstractSession(
    null,
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
