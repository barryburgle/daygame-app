package com.barryburgle.gameapp.dao.date

import androidx.room.Dao
import androidx.room.Query
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.stat.AggregatedDates
import kotlinx.coroutines.flow.Flow

@Dao
interface AggregatedDatesDao {

    companion object {
        const val QUERY_DATES_BY_WEEKS =
            "SELECT COUNT(*) as dates, CAST(strftime('%Y', meeting_date) as INTEGER) as year_number, CAST(week_number as INTEGER) as period_number, '[' || strftime('%d', date(meeting_date, '-6 days', 'weekday 0')) || '-' || strftime('%d', date(meeting_date, '-6 days', 'weekday 0', '+6 days')) || ']\n' || strftime('%m', date(meeting_date, '-6 days', 'weekday 0')) || '-' || substr(strftime('%Y', date(meeting_date, '-6 days', 'weekday 0')), 3, 2) as label, SUM(date_time)/60 as date_time_spent from meeting GROUP BY strftime('%Y', meeting_date), week_number"
        const val QUERY_DATES_BY_MONTHS =
            "SELECT COUNT(*) as dates, CAST(strftime('%Y', meeting_date) as INTEGER) as year_number, CAST(strftime('%m', meeting_date) as INTEGER) as period_number, CAST(strftime('%Y', meeting_date) as INTEGER) || '-' || CAST(strftime('%m', meeting_date) as INTEGER) as label, SUM(date_time)/60 as date_time_spent from meeting GROUP BY strftime('%Y', meeting_date), strftime('%m', meeting_date)"
    }

    @Query(QUERY_DATES_BY_WEEKS)
    fun groupStatsByWeekNumber(): Flow<List<AggregatedDates>>

    @Query(QUERY_DATES_BY_MONTHS)
    fun groupStatsByMonth(): Flow<List<AggregatedDates>>
}