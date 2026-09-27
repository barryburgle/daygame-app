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
}