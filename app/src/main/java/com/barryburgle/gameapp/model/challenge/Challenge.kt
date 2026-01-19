package com.barryburgle.gameapp.model.challenge

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.game.EventModel
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import java.time.DayOfWeek
import java.util.Locale

@Entity(tableName = "challenge")
open class Challenge(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "description") var description: String?, // TODO: not sure this is necessary: goal and type should be enough for describing a challenge
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
        if (name != null) {
            return name?.replaceFirstChar { it.uppercase() }.toString()
        }
        return EventTypeEnum.CHALLENGE.getField()
    }

    override fun getEventIcon(): ImageVector {
        return EventTypeEnum.CHALLENGE.getIcon()!!
    }

    override fun getEventDescription(): String {
        return "${
            DayOfWeek.of(dayOfWeek).toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } ${FormatService.getDate(startDate)} until ${
            DayOfWeek.of(FormatService.parseDate(endDate).dayOfWeek.value).toString().lowercase()
        } ${
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
        return ""
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