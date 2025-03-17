package com.barryburgle.gameapp.model.stat

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class AggregatedDates(
    @ColumnInfo(name = "dates") var dates: Float,
    @ColumnInfo(name = "period_number") override var periodNumber: Int,
    @ColumnInfo(name = "date_time_spent") var dateTimeSpent: Float
) : PeriodAware