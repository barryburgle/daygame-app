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
    @ColumnInfo(name = "local_timestamp") var localTimestamp: String,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double,
    @ColumnInfo(name = "day_of_week") var dayOfWeek: Int
) {
    /*InsertTime-agnostic equals*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PinPoint) return false

        return id == other.id &&
                sourceEventId == other.sourceEventId &&
                sourceEventType == other.sourceEventType &&
                pinPointType == other.pinPointType &&
                localTimestamp == other.localTimestamp &&
                latitude == other.latitude &&
                longitude == other.longitude &&
                dayOfWeek == other.dayOfWeek
    }

    /*InsertTime-agnostic hashCode*/
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + sourceEventId.hashCode()
        result = 31 * result + sourceEventType.hashCode()
        result = 31 * result + pinPointType.hashCode()
        result = 31 * result + localTimestamp.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + dayOfWeek
        return result
    }
}