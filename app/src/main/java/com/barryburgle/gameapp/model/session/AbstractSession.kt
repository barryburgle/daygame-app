package com.barryburgle.gameapp.model.session

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

abstract class AbstractSession(
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
) {

    val insertTime = insertTime
    val date = date
    val startHour = startHour
    val endHour = endHour
    val sets = sets
    val convos = convos
    val contacts = contacts
    val stickingPoints = stickingPoints
    val sessionTime = sessionTime
    val approachTime = approachTime
    val convoRatio = convoRatio
    val rejectionRatio = rejectionRatio
    val contactRatio = contactRatio
    val index = index
    val dayOfWeek = dayOfWeek
    val weekNumber = weekNumber
}