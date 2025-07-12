package com.barryburgle.gameapp.model.session

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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

@Entity(tableName = "abstract_session")
open class AbstractSession(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "session_date") var date: String,
    @ColumnInfo(name = "start_hour") var startHour: String,
    @ColumnInfo(name = "end_hour") var endHour: String,
    @ColumnInfo(name = "sets") var sets: Int,
    @ColumnInfo(name = "convos") var convos: Int,
    @ColumnInfo(name = "contacts") var contacts: Int,
    @ColumnInfo(name = "sticking_points") var stickingPoints: String,
    @ColumnInfo(name = "session_time") var sessionTime: Long,
    @ColumnInfo(name = "approach_time") var approachTime: Long,
    @ColumnInfo(name = "convo_ratio") var convoRatio: Double,
    @ColumnInfo(name = "rejection_ratio") var rejectionRatio: Double,
    @ColumnInfo(name = "contact_ratio") var contactRatio: Double,
    @ColumnInfo(name = "index") var index: Double,
    @ColumnInfo(name = "day_of_week") var dayOfWeek: Int,
    @ColumnInfo(name = "week_number") var weekNumber: Int
) : EventModel {
    override fun getEventDate(): String {
        return date
    }

    override fun getEventTitle(): String {
        return EventTypeEnum.SESSION.getField()
    }

    override fun getEventIcon(): ImageVector {
        return Icons.Filled.CalendarToday
    }

    override fun getEventDescription(): String {
        return "${
            DayOfWeek.of(dayOfWeek).toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } ${
            FormatService.getTime(
                startHour
            )
        } - ${
            FormatService.getTime(
                endHour
            )
        } : ${sessionTime} minutes"
    }

    override fun getEventStickingPoints(): String {
        return stickingPoints
    }

    override fun shareReport(leads: List<Lead>): String {
        return "\uD83D\uDCC5 ${date.dropLast(7)} ${sessionTime}' session report:\n•${sets} set${
            pluralMaker(
                sets
            )
        }\n•${convos} conversation${pluralMaker(convos)}\n•${contacts} contact${pluralMaker(contacts)}${
            reportLeads(
                leads
            )
        }${stickingPointsReport(stickingPoints)}"
    }
}