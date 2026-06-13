package com.barryburgle.gameapp.model.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinpoint")
open class PinPoint(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "source_event_id") var sourceEventId: Long,
    @ColumnInfo(name = "source_event_type") var sourceEventType: String,
    @ColumnInfo(name = "pinpoint_type") var pinPointType: String,
    @ColumnInfo(name = "utc_timestamp") var utcTimestamp: String,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double
)