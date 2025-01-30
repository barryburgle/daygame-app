package com.barryburgle.gameapp.model.stat

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class CountLead(
    @ColumnInfo(name = "count") var count: Int,
)