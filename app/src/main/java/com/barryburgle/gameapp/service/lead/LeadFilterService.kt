package com.barryburgle.gameapp.service.lead

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateTypeEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.ping.Ping
import com.barryburgle.gameapp.model.ping.SentPing
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.ui.output.dialog.LeadTimelineEvent
import java.time.OffsetDateTime

class LeadFilterService {

    companion object {

        fun buildLeadTimelineEvents(
            lead: Lead,
            allSessions: List<AbstractSession> = emptyList(),
            allSets: List<SingleSet> = emptyList(),
            allDates: List<Date> = emptyList(),
            allPings: List<Ping> = emptyList(),
            allSentPings: List<SentPing> = emptyList(),
            allPinPoints: List<PinPoint> = emptyList()
        ): List<LeadTimelineEvent> {
            val leadId = lead.id
            val events = mutableListOf<LeadTimelineEvent>()

            val matchedSession = lead.sessionId?.let { sessionId ->
                allSessions.firstOrNull { it.id == sessionId }
            }
            val matchedSets = allSets.filter { it.leadId == leadId }
            val matchedDates = allDates.filter { it.leadId == leadId }
            val matchedSentPings = allSentPings.filter { it.leadId == leadId }

            val metPinpoint = allPinPoints.firstOrNull { it.id == lead.pinPointId }

            val metTimestamp =
                metPinpoint?.localTimestamp?.takeIf { it.isNotBlank() } ?: lead.insertTime
            if (metTimestamp.isNotBlank()) {
                events += LeadTimelineEvent.MetPinpoint(
                    timestamp = metTimestamp,
                    description = "Met ${lead.name}",
                    latitude = metPinpoint?.latitude,
                    longitude = metPinpoint?.longitude
                )
            }

            matchedSession?.let { session ->
                if (session.startHour.isNotBlank()) {
                    events += LeadTimelineEvent.SessionEvent(
                        timestamp = session.startHour,
                        description = "Session: ${session.sets} sets, ${session.convos} convos, ${session.contacts} contacts"
                    )
                }
            }

            matchedSets.forEach { set ->
                val timestamp = if (set.startHour.isNotBlank()) set.startHour else set.insertTime
                if (timestamp.isNotBlank()) {
                    val details = listOfNotNull(
                        if (set.conversation) "talked" else null,
                        if (set.contact) "closed" else null,
                        if (set.instantDate) "instant date" else null,
                        if (set.recorded) "recorded" else null
                    ).joinToString(", ")
                    events += LeadTimelineEvent.SetEvent(
                        timestamp = timestamp,
                        description = "Set" + if (details.isNotBlank()) ": $details" else ""
                    )
                }
            }

            matchedDates.forEach { date ->
                val timestamp = if (date.startHour.isNotBlank()) date.startHour else date.insertTime
                if (timestamp.isNotBlank()) {
                    val outcomes = listOfNotNull(
                        if (date.pull) "pulled" else null,
                        if (date.bounce) "bounced" else null,
                        if (date.kiss) "kissed" else null,
                        if (date.lay) "laid" else null,
                        if (date.recorded) "recorded" else null
                    ).joinToString(", ")
                    val dateType = date.dateType.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }
                    val dateLabel = if (date.dateNumber > 0) {
                        "${DateTypeEnum.getDateNumber(date.dateNumber, true)} · $dateType"
                    } else {
                        "$dateType date"
                    }
                    events += LeadTimelineEvent.DateEvent(
                        timestamp = timestamp,
                        description = dateLabel + if (outcomes.isNotBlank()) " ($outcomes)" else ""
                    )
                }
            }

            matchedSentPings.forEach { sentPing ->
                val timestamp = sentPing.sentHour.takeIf { it.isNotBlank() } ?: return@forEach
                val ping = allPings.firstOrNull { it.id == sentPing.pingId }
                events += LeadTimelineEvent.PingEvent(
                    timestamp = timestamp,
                    description = "Ping: ${ping?.title ?: "Ping"}"
                )
            }

            return events.sortedBy { parseTimestamp(it.timestamp) ?: OffsetDateTime.MIN }
        }

        fun parseTimestamp(dateStr: String): OffsetDateTime? {
            return try {
                val normalized = if (dateStr.length >= 19) dateStr else dateStr.take(16) + "Z"
                OffsetDateTime.parse(normalized)
            } catch (e: Exception) {
                null
            }
        }
    }
}