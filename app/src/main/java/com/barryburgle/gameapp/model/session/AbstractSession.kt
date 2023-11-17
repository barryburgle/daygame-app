package com.barryburgle.gameapp.model.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
) {
    companion object {
        fun header(): String {
            return "Id;InsertTime;Date;StartHour;EndHour;Sets;Convos;Contacts;StickingPoints;SessionTime;ApproachTime;ConvoRatio;RejectionRatio;ContactRatio;Index;DayOfWeek;WeekNumber"
        }
    }

    override fun toString(): String {
        return "${id};${insertTime};${date};${startHour};${endHour};${sets};${convos};${contacts};${
            stickingPoints.replace(
                "\n",
                " "
            )
        };${sessionTime};${approachTime};${convoRatio};${rejectionRatio};${contactRatio};${index};${dayOfWeek};${weekNumber}"
    }
}