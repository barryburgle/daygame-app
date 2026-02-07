package com.barryburgle.gameapp.model.challenge

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Embedded
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