package com.barryburgle.gameapp.model.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting")
open class Setting(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo(name = "value") var value: String
)