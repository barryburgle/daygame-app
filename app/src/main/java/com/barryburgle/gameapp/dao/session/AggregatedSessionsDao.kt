package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Query
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.stat.AggregatedSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface AggregatedSessionsDao {

    companion object {
        const val QUERY_SESSIONS_BY_WEEKS =
            "SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, CAST(strftime('%Y', session_date) as INTEGER) as year_number, CAST(week_number as INTEGER) as period_number, '[' || strftime('%d', date(session_date, '-6 days', 'weekday 0')) || '-' || strftime('%d', date(session_date, '-6 days', 'weekday 0', '+6 days')) || ']\n' || strftime('%m', date(session_date, '-6 days', 'weekday 0')) || '-' || substr(strftime('%Y', date(session_date, '-6 days', 'weekday 0')), 3, 2) as label, SUM(session_time)/60 as time_spent from abstract_session GROUP BY strftime('%Y', session_date), week_number"
        const val QUERY_SESSIONS_BY_MONTHS=
            "SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, CAST(strftime('%Y', session_date) as INTEGER) as year_number, CAST(strftime('%m', session_date) as INTEGER) as period_number, CAST(strftime('%Y', session_date) as INTEGER) || '-' || CAST(strftime('%m', session_date) as INTEGER) as label, SUM(session_time)/60 as time_spent from abstract_session GROUP BY strftime('%Y', session_date), strftime('%m', session_date)"
    }

    @Query(QUERY_SESSIONS_BY_WEEKS)
    fun groupStatsByWeekNumber(): Flow<List<AggregatedSessions>>

    @Query(QUERY_SESSIONS_BY_MONTHS)
    fun groupStatsByMonth(): Flow<List<AggregatedSessions>>
}