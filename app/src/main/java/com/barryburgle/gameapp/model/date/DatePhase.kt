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
}