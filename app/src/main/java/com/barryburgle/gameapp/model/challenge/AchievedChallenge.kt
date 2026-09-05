package com.barryburgle.gameapp.model.challenge

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Embedded
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.model.game.EventModel
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import java.time.LocalDate
import java.time.temporal.ChronoUnit

open class AchievedChallenge(
    @Embedded
    val challenge: Challenge,
    val achieved: Double = 0.0
) : EventModel {
    override fun getEventDate(): String? {
        return challenge.getEventDate()
    }

    override fun getEventTitle(): String {
        return challenge.getEventTitle()
    }

    override fun getEventIcon(): ImageVector {
        return challenge.getEventIcon()
    }

    override fun getHeaderWeekday(): String {
        return challenge.getHeaderWeekday()
    }

    override fun getHeaderDate(): String {
        return challenge.getHeaderDate()
    }

    override fun getHeaderTime(): String {
        return challenge.getHeaderTime()
    }

    override fun getHeaderDuration(): String {
        return challenge.getHeaderDuration()
    }

    override fun getEventStickingPoints(): String? {
        return challenge.getEventStickingPoints()
    }

    override fun shareReport(leads: List<Lead>): String {
        return challenge.shareReport(leads)
    }

    fun getCompletionPerc(): Double {
        return achieved / challenge.goal
    }

    fun getTimePassingPerc(): Double {
        val timePassed = ChronoUnit.DAYS.between(
            FormatService.parseDate(challenge.startDate),
            LocalDate.now()
        ) + 1
        val totalTime = challenge.getTotalDays()
        return timePassed.toDouble() / totalTime.toDouble()
    }

    fun getAchievedChallengeReport(isCurrentChallengeSummary: Boolean): String {
        var completePercentage = getCompletionPerc()
        var reportPrefix = if (isCurrentChallengeSummary) "• Current " else "\uD83C\uDFC6 New "
        var report = reportPrefix + shareReport(emptyList())
        var achievedToPrint = achieved.toString()
        if (ChallengeTypeEnum.isTypeAchievedInteger(
                challenge.type
            )
        ) {
            achievedToPrint = achieved.toInt().toString()
        }
        val achievedPrefix =
            "\n\nAchieved: ${achievedToPrint}/${challenge.goal} ${challenge.type}s\n"
        if (completePercentage >= 1) {
            report += achievedPrefix + "████████████████████ 100%"
        } else {
            completePercentage *= 20
            val intCompletePercentage = completePercentage.toInt()
            var completionBar = ""
            for (i in 1..intCompletePercentage) {
                completionBar += "█"
            }
            for (i in 1..(20 - intCompletePercentage)) {
                completionBar += "░"
            }
            completionBar += " ${intCompletePercentage * 5}%"
            report += achievedPrefix + completionBar
        }
        return report
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AchievedChallenge) return false

        // TODO: ignoring "achieved" on equality, defer this to when the achieved column will be re-computed on import
        // Delegates equality to the Challenge class ignoring the 'achieved' property
        return challenge == other.challenge
    }

    override fun hashCode(): Int {
        // Ensure hashCode also ignores the 'achieved' property
        return challenge.hashCode()
    }

    constructor() : this(
        Challenge(
            0,
            "",
            null,
            null,
            "",
            "",
            "",
            0,
            "",
            1
        )
    )
}