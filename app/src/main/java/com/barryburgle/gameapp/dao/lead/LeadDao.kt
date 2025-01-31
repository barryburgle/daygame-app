package com.barryburgle.gameapp.dao.lead

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.stat.CountLead
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(lead: Lead)

    @Delete
    suspend fun delete(lead: Lead)

    @Query("SELECT * from lead")
    fun getAll(): Flow<List<Lead>>

    // TODO: use the following function in future to fetch leads on session cards
    @Query("SELECT * from lead WHERE session_id = :sessionId")
    fun getBySessionId(sessionId: Long): Flow<List<Lead>>

    @Query("SELECT * from lead ORDER BY insert_time DESC")
    fun getByInsertTime(): Flow<List<Lead>>

    @Query("SELECT COUNT(*) as count from lead GROUP BY contact")
    fun groupByContact(): Flow<List<CountLead>>

    @Query("SELECT COUNT(*) as count from lead GROUP BY nationality")
    fun groupByNationality(): Flow<List<CountLead>>
}