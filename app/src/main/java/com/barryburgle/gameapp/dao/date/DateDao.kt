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
    fun getByDate(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY lead_id DESC, meeting_date DESC")
    fun getByLead(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY location DESC, meeting_date DESC")
    fun getByLocation(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY start_time DESC, meeting_date DESC")
    fun getByStartTime(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY end_time DESC, meeting_date DESC")
    fun getByEndTime(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY cost DESC, meeting_date DESC")
    fun getByCost(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY date_number DESC, meeting_date DESC")
    fun getByDateNumber(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE pull = 1 ORDER BY meeting_date DESC")
    fun getPulled(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE pull = 0 ORDER BY meeting_date DESC")
    fun getNotPulled(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE bounce = 1 ORDER BY meeting_date DESC")
    fun getBounced(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE bounce = 0 ORDER BY meeting_date DESC")
    fun getNotBounced(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE kiss = 1 ORDER BY meeting_date DESC")
    fun getKissed(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE kiss = 0 ORDER BY meeting_date DESC")
    fun getNotKissed(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE lay = 1 ORDER BY meeting_date DESC")
    fun getLaid(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE lay = 0 ORDER BY meeting_date DESC")
    fun getNotLaid(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE recorded = 1 ORDER BY meeting_date DESC")
    fun getRecorded(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE recorded = 0 ORDER BY meeting_date DESC")
    fun getNotRecorded(): Flow<List<Date>>
}