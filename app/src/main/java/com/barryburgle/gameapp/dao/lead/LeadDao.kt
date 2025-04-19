package com.barryburgle.gameapp.dao.lead

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(leads: List<Lead>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(lead: Lead): Long

    @Delete
    suspend fun delete(lead: Lead)

    @Query("SELECT * from lead ORDER BY session_id DESC, insert_time DESC")
    fun getAll(): Flow<List<Lead>>

    @Query("SELECT nationality as category, COUNT(*) as frequency from lead GROUP BY nationality ORDER BY nationality")
    fun getNationalityHistogram(): Flow<List<CategoryHistogram>>

    @Query("SELECT age as metric, COUNT(*) as frequency from lead GROUP BY age ORDER BY age")
    fun getAgeHistogram(): Flow<List<Histogram>>
}