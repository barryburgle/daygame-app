package com.barryburgle.gameapp.model.stat

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class AggregatedStat(
    @ColumnInfo(name = "sets") var sets: Float,
    @ColumnInfo(name = "convos") var convos: Float,
    @ColumnInfo(name = "contacts") var contacts: Float,
    @ColumnInfo(name = "avg_index") var avgIndex: Float,
    @ColumnInfo(name = "avg_convo_ratio") var avgConvoRatio: Float,
    @ColumnInfo(name = "avg_contact_ratio") var avgContactRatio: Float,
    @ColumnInfo(name = "period_number") var periodNumber: Int,
    @ColumnInfo(name = "time_spent") var timeSpent: Float
)