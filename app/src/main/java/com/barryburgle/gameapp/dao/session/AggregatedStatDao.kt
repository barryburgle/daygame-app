package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Query
import com.barryburgle.gameapp.model.stat.AggregatedStat
import kotlinx.coroutines.flow.Flow

@Dao
interface AggregatedStatDao {

    @Query("SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, week_number as period_number from abstract_session GROUP BY week_number")
    fun groupStatsByWeekNumber(): Flow<List<AggregatedStat>>

    @Query("SELECT SUM(sets) as sets, SUM(convos) as convos, SUM(contacts) as contacts, AVG(`index`) as avg_index, AVG(convo_ratio) as avg_convo_ratio, AVG(contact_ratio)  as avg_contact_ratio, strftime('%m', session_date) as period_number from abstract_session GROUP BY strftime('%m', session_date)")
    fun groupStatsByMonth(): Flow<List<AggregatedStat>>
}