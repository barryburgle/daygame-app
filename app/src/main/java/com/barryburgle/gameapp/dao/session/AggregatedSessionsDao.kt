package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Query
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.stat.AggregatedSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface AggregatedSessionsDao {

    @Query("SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, week_number as period_number, SUM(session_time)/60 as time_spent from abstract_session GROUP BY week_number LIMIT (" + SettingDao.QUERY_LAST_WEEKS_SHOWN +")")
    fun groupStatsByWeekNumber(): Flow<List<AggregatedSessions>>

    @Query("SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, strftime('%m', session_date) as period_number, SUM(session_time)/60 as time_spent from abstract_session GROUP BY strftime('%m', session_date) LIMIT (" + SettingDao.QUERY_LAST_MONTHS_SHOWN +")")
    fun groupStatsByMonth(): Flow<List<AggregatedSessions>>
}