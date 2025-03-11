package com.barryburgle.gameapp.model.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting")
open class Date(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "lead_id") var leadId: Long?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "meeting_date") var date: String?,
    @ColumnInfo(name = "start_time") var startTime: String?,
    @ColumnInfo(name = "end_time") var endTime: String?,
    @ColumnInfo(name = "cost") var cost: Double?,
    @ColumnInfo(name = "date_number") var dateNumber: Long,
    @ColumnInfo(name = "date_type") var dateType: String,
    @ColumnInfo(name = "pull") var pull: Boolean,
    @ColumnInfo(name = "bounce") var bounce: Boolean,
    @ColumnInfo(name = "kiss") var kiss: Boolean,
    @ColumnInfo(name = "lay") var lay: Boolean,
    @ColumnInfo(name = "recorded") var recorded: Boolean,
    @ColumnInfo(name = "sticking_points") var stickingPoints: String?,
    @ColumnInfo(name = "tweet_url") var tweetUrl: String?
) {
    constructor() : this(0, null, "", "", "", "", 0.0, 0, "",false, false, false, false, false, "", "")
}