package com.barryburgle.gameapp.model.ping

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ping")
open class Ping(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "body") var body: String? = null,
    @ColumnInfo(name = "link") var link: String? = null,
    @ColumnInfo(name = "pic") var pic: String? = null,
    @ColumnInfo(name = "audio") var audio: String? = null, // TODO: implement audio acquisition in highest quality
    @ColumnInfo(name = "lead_ids") var leadIds: List<Long> = emptyList()
) {
    constructor() : this(0, "", null, null, null, null, emptyList())
}