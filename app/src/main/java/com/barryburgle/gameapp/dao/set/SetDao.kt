package com.barryburgle.gameapp.dao.set

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.set.SingleSet
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(sets: List<SingleSet>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(singleSet: SingleSet): Long

    @Delete
    suspend fun delete(singleSet: SingleSet)

    @Query("SELECT * from single_set ORDER BY id DESC, set_date DESC, start_hour DESC")
    fun getAll(): Flow<List<SingleSet>>

    // TODO: do other sorting and histogram methods
}