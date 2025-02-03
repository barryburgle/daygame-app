package com.barryburgle.gameapp.model.stat

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class CategoryHistogram(
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "frequency") var frequency: Float
)