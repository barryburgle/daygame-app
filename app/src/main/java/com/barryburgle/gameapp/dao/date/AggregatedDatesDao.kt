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
            "SELECT COUNT(*) as dates, CAST(strftime('%Y', meeting_date) as INTEGER) as year_number, CAST(week_number as INTEGER) as period_number, SUM(date_time)/60 as date_time_spent from meeting WHERE (strftime('%Y', meeting_date), period_number) IN (${SettingDao.QUERY_LAST_WEEKS}) GROUP BY strftime('%Y', meeting_date), week_number"
        const val QUERY_DATES_BY_MONTHS =
            "SELECT COUNT(*) as dates, CAST(strftime('%Y', meeting_date) as INTEGER) as year_number, CAST(strftime('%m', meeting_date) as INTEGER) as period_number, SUM(date_time)/60 as date_time_spent from meeting WHERE (strftime('%Y', meeting_date), period_number) IN (${SettingDao.QUERY_LAST_MONTHS}) GROUP BY strftime('%Y', meeting_date), strftime('%m', meeting_date)"
    }

    @Query(QUERY_DATES_BY_WEEKS)
    fun groupStatsByWeekNumber(): Flow<List<AggregatedDates>>

    @Query(QUERY_DATES_BY_MONTHS)
    fun groupStatsByMonth(): Flow<List<AggregatedDates>>
}