package com.barryburgle.gameapp.model.date

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.game.EventModel
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import java.time.DayOfWeek
import java.util.Locale

@Entity(tableName = "meeting")
open class Date(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "lead_id") var leadId: Long?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "meeting_date") var date: String?,
    @ColumnInfo(name = "start_hour") var startHour: String,
    @ColumnInfo(name = "end_hour") var endHour: String,
    @ColumnInfo(name = "cost") var cost: Int?,
    @ColumnInfo(name = "date_number") var dateNumber: Int,
    @ColumnInfo(name = "date_type") var dateType: String,
    @ColumnInfo(name = "pull") var pull: Boolean,
    @ColumnInfo(name = "bounce") var bounce: Boolean,
    @ColumnInfo(name = "kiss") var kiss: Boolean,
    @ColumnInfo(name = "lay") var lay: Boolean,
    @ColumnInfo(name = "recorded") var recorded: Boolean,
    @ColumnInfo(name = "sticking_points") var stickingPoints: String?,
    @ColumnInfo(name = "tweet_url") var tweetUrl: String?,
    @ColumnInfo(name = "date_time") var dateTime: Long,
    @ColumnInfo(name = "day_of_week") var dayOfWeek: Int,
    @ColumnInfo(name = "week_number") var weekNumber: Int
) : EventModel {
    override fun getEventDate(): String? {
        return date
    }

    override fun getEventTitle(): String {
        return "${dateType.replaceFirstChar { it.uppercase() }} ${EventTypeEnum.DATE.getField()}"
    }

    override fun getEventIcon(): ImageVector {
        return DateType.getIcon(dateType)
    }

    override fun getEventDescription(): String {
        return "${
            DayOfWeek.of(dayOfWeek).toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } ${FormatService.getDate(date)} ${
            FormatService.getTime(
                startHour
            )
        } - ${
            FormatService.getTime(
                endHour
            )
        }"
    }

    override fun getEventDuration(): String {
        return "${dateTime} minutes"
    }

    override fun getEventStickingPoints(): String? {
        return stickingPoints
    }


    override fun shareReport(leads: List<Lead>): String {
        return shareDateReport(leads, false)
    }


    fun shareDateReport(leads: List<Lead>, simplePlusOneReport: Boolean): String {
        var sharedDateNumber = DateType.getDateNumber(
            dateNumber,
            true
        )
        if (dateNumber == 0 && lay) {
            sharedDateNumber = "SDL"
        }
        var dateDuration = ""
        if (!lay) {
            dateDuration = " ${dateTime}'"
        }
        var plusOnePrefix = ""
        if (lay) {
            plusOnePrefix = "+1 "
        }
        var dateReport = "${plusOnePrefix}${
            reportLeads(
                leads
            ).drop(1)
        }${dateDuration} ${DateType.getEmoji(dateType)} ${
            sharedDateNumber
        }"
        if (simplePlusOneReport) {
            return dateReport
        }
        dateReport = dateReport +
                " report:\n• pulled${
                    checkmarkReport(
                        pull
                    )
                }\n• bounced${checkmarkReport(bounce)}\n• kissed${checkmarkReport(kiss)}\n• laid${
                    checkmarkReport(
                        lay
                    )
                }\n• recorded${checkmarkReport(recorded)}${stickingPointsReport(stickingPoints)}"
        return dateReport
    }

    constructor() : this(
        0,
        "",
        null,
        "",
        "",
        "",
        "",
        0,
        0,
        "",
        false,
        false,
        false,
        false,
        false,
        "",
        "",
        0L,
        1,
        1
    )
}