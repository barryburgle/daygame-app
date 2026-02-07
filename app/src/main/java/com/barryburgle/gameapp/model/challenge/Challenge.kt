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
        return "${FormatService.getDate(startDate)} ⮕ ${
            FormatService.getDate(
                endDate
            )
        }"
    }

    override fun getEventDuration(): String {
        if (!description!!.isBlank()) {
            return description!!
        }
        return ""
    }

    override fun getEventStickingPoints(): String? {
        return ""
    }

    override fun shareReport(leads: List<Lead>): String {
        var exportPrefix = "New \"" + getEventTitle() + "\""
        if (!EventTypeEnum.CHALLENGE.getField().equals(exportPrefix)) {
            exportPrefix += " " + EventTypeEnum.CHALLENGE.getField().lowercase()
        }
        return "\uD83C\uDFC6 ${exportPrefix}\n${FormatService.getDate(startDate)} until ${
            FormatService.getDate(
                endDate
            )
        }\n\nGoal: ${goal} ${type.replaceFirstChar { it.lowercase() }}s\n\n${description}"
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