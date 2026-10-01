package com.barryburgle.gameapp.model.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.barryburgle.gameapp.database.Converters

@Entity(tableName = "date_model")
@TypeConverters(Converters::class)
open class DateModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "phases") var phases: List<Long> = emptyList()
) {
    constructor() : this(0, "", "", emptyList())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateModel) return false

        return id == other.id &&
                title == other.title &&
                description == other.description &&
                phases == other.phases
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + phases.hashCode()
        return result
    }
}