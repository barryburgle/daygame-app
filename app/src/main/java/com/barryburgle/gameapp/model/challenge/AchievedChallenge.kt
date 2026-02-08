package com.barryburgle.gameapp.model.challenge

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Embedded
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.model.game.EventModel
import com.barryburgle.gameapp.model.lead.Lead

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

    override fun getEventDescription(): String {
        return challenge.getEventDescription()
    }

    override fun getEventDuration(): String {
        return challenge.getEventDuration()
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
            "\n\nAchieved: ${achievedToPrint} ${challenge.type}s\n"
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