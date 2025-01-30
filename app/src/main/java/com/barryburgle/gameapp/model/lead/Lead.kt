package com.barryburgle.gameapp.model.lead

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lead")
open class Lead(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "session_id") var sessionId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "contact") var contact: String,
    @ColumnInfo(name = "nationality") var nationality: String,
    @ColumnInfo(name = "age") var age: Long
) {
    constructor() : this(0, "", null, "", "", "", 20)
}