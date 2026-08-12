package com.barryburgle.gameapp.model.set

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
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

@Entity(tableName = "single_set")
open class SingleSet(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "set_date") var date: String,
    @ColumnInfo(name = "start_hour") var startHour: String,
    @ColumnInfo(name = "end_hour") var endHour: String,
    @ColumnInfo(name = "session_id") var sessionId: Long?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "conversation") var conversation: Boolean,
    @ColumnInfo(name = "contact") var contact: Boolean,
    @ColumnInfo(name = "instant_date") var instantDate: Boolean,
    @ColumnInfo(name = "recorded") var recorded: Boolean,
    @ColumnInfo(name = "lead_id") var leadId: Long?,
    @ColumnInfo(name = "date_id") var dateId: Long?,
    @ColumnInfo(name = "sticking_points") var stickingPoints: String,
    @ColumnInfo(name = "tweet_url") var tweetUrl: String?,
    @ColumnInfo(name = "set_time") var setTime: Long,
    @ColumnInfo(name = "day_of_week") var dayOfWeek: Int,
    @ColumnInfo(name = "week_number") var weekNumber: Int
) : EventModel {
    override fun getEventDate(): String {
        return date
    }

    override fun getEventTitle(): String {
        return EventTypeEnum.SET.getField()
    }

    override fun getEventIcon(): ImageVector {
        return Icons.Filled.ChatBubbleOutline
    }

    override fun getEventDescription(): String {
        return "${
            DayOfWeek.of(dayOfWeek).toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } ${FormatService.getDate(date)}"
    }

    override fun getEventDuration(): String {
        return "${
            FormatService.getTime(
                startHour
            )
        } - ${
            FormatService.getTime(
                endHour
            )
        }: ${setTime} minutes"
    }

    override fun getEventStickingPoints(): String {
        return stickingPoints
    }

    override fun shareReport(leads: List<Lead>): String {
        return "${
            reportLeads(
                leads
            ).drop(1)
        } ${setTime}' set report:\n• talked${
            checkmarkReport(
                conversation
            )
        }\n• closed${checkmarkReport(contact)}\n• idated${checkmarkReport(instantDate)}\n• recorded${
            checkmarkReport(
                recorded
            )
        }${stickingPointsReport(stickingPoints)}"
    }

    /*InsertTime-agnostic equals*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SingleSet) return false

        return id == other.id &&
                date == other.date &&
                startHour == other.startHour &&
                endHour == other.endHour &&
                sessionId == other.sessionId &&
                location == other.location &&
                conversation == other.conversation &&
                contact == other.contact &&
                instantDate == other.instantDate &&
                recorded == other.recorded &&
                leadId == other.leadId &&
                dateId == other.dateId &&
                stickingPoints == other.stickingPoints &&
                tweetUrl == other.tweetUrl &&
                setTime == other.setTime &&
                dayOfWeek == other.dayOfWeek &&
                weekNumber == other.weekNumber
    }

    /*InsertTime-agnostic hashCode*/
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + date.hashCode()
        result = 31 * result + startHour.hashCode()
        result = 31 * result + endHour.hashCode()
        result = 31 * result + (sessionId?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + conversation.hashCode()
        result = 31 * result + contact.hashCode()
        result = 31 * result + instantDate.hashCode()
        result = 31 * result + recorded.hashCode()
        result = 31 * result + (leadId?.hashCode() ?: 0)
        result = 31 * result + (dateId?.hashCode() ?: 0)
        result = 31 * result + stickingPoints.hashCode()
        result = 31 * result + (tweetUrl?.hashCode() ?: 0)
        result = 31 * result + setTime.hashCode()
        result = 31 * result + dayOfWeek
        result = 31 * result + weekNumber
        return result
    }

    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        null,
        null,
        false,
        false,
        false,
        false,
        null,
        null,
        "",
        "",
        0L,
        1,
        1
    )
}