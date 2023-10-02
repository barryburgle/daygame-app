package com.barryburgle.gameapp.model.session

import com.barryburgle.gameapp.service.AbstractSessionService
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
    stickingPoints: String
) {
    val insertTime = insertTime
    val date = date
    val startHour = startHour
    val endHour = endHour
    val sets = sets
    val convos = convos
    val contacts = contacts
    val stickingPoints = stickingPoints
    val sessionTime: Long
    val approachTime: Long
    val convoRatio: Double
    val rejectionRatio: Double
    val contactRatio: Double
    val index: Double
    val dayOfWeek: DayOfWeek
    val weekNumber: Int

    init {
        sessionTime = AbstractSessionService.computeSessionTime(startHour, endHour)
        approachTime = AbstractSessionService.computeApproachTime(sessionTime, sets)
        convoRatio = AbstractSessionService.computeConvoRatio(convos, sets)
        rejectionRatio = AbstractSessionService.computeRejectionRatio(convos, sets)
        contactRatio = AbstractSessionService.computeContactRatio(contacts, sets)
        index = AbstractSessionService.computeIndex(sets, convos, contacts, sessionTime)
        dayOfWeek = AbstractSessionService.computeDayOfWeek(date)
        weekNumber = AbstractSessionService.computeWeekOfYear(date)
    }

    fun getYearAndWeek(liveSession: LiveSession, weekNumber: Int): Pair<Int, Int> {
        // TODO: method useful tot track weekly total sets + convos + contacts
        val year = liveSession.date.year
        return year to weekNumber
    }
}