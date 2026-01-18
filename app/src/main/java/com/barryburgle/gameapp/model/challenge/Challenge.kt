package com.barryburgle.gameapp.model.challenge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "tweet_url") var tweetUrl: String?
)