package com.barryburgle.gameapp.model.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
) {
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