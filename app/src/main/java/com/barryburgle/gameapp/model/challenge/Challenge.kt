package com.barryburgle.gameapp.model.challenge

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.game.EventModel
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService

@Entity(tableName = "challenge")
open class Challenge(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "start_date") var startDate: String,
    @ColumnInfo(name = "end_date") var endDate: String,
    @ColumnInfo(name = "challenge_type") var type: String,
    @ColumnInfo(name = "goal") var goal: Int,
    @ColumnInfo(name = "tweet_url") var tweetUrl: String?,
    @ColumnInfo(name = "day_of_week") var dayOfWeek: Int
) : EventModel {
    override fun getEventDate(): String? {
        return startDate
    }

    override fun getEventTitle(): String {
        return EventTypeEnum.CHALLENGE.getField()
    }

    override fun getEventIcon(): ImageVector {
        return ChallengeTypeEnum.getIcon(type)
    }

    override fun getEventDescription(): String {
        if (!name!!.isBlank()) {
            return "\"" + name?.replaceFirstChar { it.uppercase() } + "\""
        }
        return ""
    }

    override fun getEventDuration(): String {
        return "${FormatService.getDate(startDate)} ⮕ ${
            FormatService.getDate(
                endDate
            )
        }"
    }

    override fun getEventStickingPoints(): String? {
        if (!description!!.isBlank()) {
            return description
        }
        return ""
    }

    override fun shareReport(leads: List<Lead>): String {
        return "${type}s' challenge: \"${name}\"\nDescription: ${description}\n\nGoal: ${goal} ${type.replaceFirstChar { it.lowercase() }}s from ${
            FormatService.getDate(
                startDate
            )
        } until ${
            FormatService.getDate(
                endDate
            )
        }"
    }

    constructor() : this(
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
}