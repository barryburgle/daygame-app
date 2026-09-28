package com.barryburgle.gameapp.dao.date

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.date.DatePhase
import kotlinx.coroutines.flow.Flow

@Dao
interface DatePhaseDao {

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(datePhases: List<DatePhase>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(datePhase: DatePhase): Long

    @Delete
    suspend fun delete(datePhase: DatePhase)

    @Query("DELETE FROM date_phase")
    suspend fun deleteAll()

    @Query("SELECT * from date_phase ORDER BY id DESC")
    fun getAll(): Flow<List<DatePhase>>
}