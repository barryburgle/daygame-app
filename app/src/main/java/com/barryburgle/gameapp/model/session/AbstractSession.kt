package com.barryburgle.gameapp.model.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "abstract_session")
abstract class AbstractSession(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "insert_time") var insertTime: Instant,
    @ColumnInfo(name = "session_date") var date: LocalDate,
    @ColumnInfo(name = "start_hour") var startHour: LocalTime,
    @ColumnInfo(name = "end_hour") var endHour: LocalTime,
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
    @ColumnInfo(name = "day_of_week") var dayOfWeek: DayOfWeek,
    @ColumnInfo(name = "week_number") var weekNumber: Int
)