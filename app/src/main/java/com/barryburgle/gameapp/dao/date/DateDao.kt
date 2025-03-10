package com.barryburgle.gameapp.dao.date

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.date.Date
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(date: Date)

    @Delete
    suspend fun delete(date: Date)

    @Query("SELECT * from meeting ORDER BY id DESC, meeting_date DESC")
    fun getAll(): Flow<List<Date>>
}