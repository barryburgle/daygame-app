package com.barryburgle.gameapp.model.stat

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class Histogram(
    @ColumnInfo(name = "metric") var metric: Float,
    @ColumnInfo(name = "frequency") var frequency: Float
)