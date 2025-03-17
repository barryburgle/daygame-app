package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Query
import com.barryburgle.gameapp.model.stat.AggregatedDates
import kotlinx.coroutines.flow.Flow

@Dao
interface AggregatedDatesDao {

    @Query("SELECT COUNT(*) as dates, week_number as period_number, SUM(date_time)/60 as date_time_spent from meeting GROUP BY week_number")
    fun groupStatsByWeekNumber(): Flow<List<AggregatedDates>>

    @Query("SELECT COUNT(*) as dates, strftime('%m', meeting_date) as period_number, SUM(date_time)/60 as date_time_spent from meeting GROUP BY strftime('%m', meeting_date)")
    fun groupStatsByMonth(): Flow<List<AggregatedDates>>
}