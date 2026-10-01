package com.barryburgle.gameapp.model.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_phase")
open class DatePhase(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "duration") var duration: Long = 15
) {
    constructor() : this(0, "", "", 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DatePhase) return false

        return id == other.id &&
                title == other.title &&
                description == other.description &&
                duration == other.duration
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}