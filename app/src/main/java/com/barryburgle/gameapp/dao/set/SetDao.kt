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

    @Query("DELETE FROM single_set")
    suspend fun deleteAll()

    @Query("SELECT * from single_set ORDER BY id DESC, set_date DESC, start_hour DESC")
    fun getAll(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set ORDER BY id DESC, set_date DESC")
    fun getByDate(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set ORDER BY id DESC, start_hour DESC")
    fun getByStartHour(): Flow<List<SingleSet>>

    // TODO: create getBySession()

    @Query("SELECT * from single_set ORDER BY id DESC, location DESC")
    fun getByLocation(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE conversation = 1 ORDER BY set_date DESC")
    fun getConversed(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE conversation = 0 ORDER BY set_date DESC")
    fun getNotConversed(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE contact = 1 ORDER BY set_date DESC")
    fun getContact(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE contact = 0 ORDER BY set_date DESC")
    fun getNotContact(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE instant_date = 1 ORDER BY set_date DESC")
    fun getInstantDated(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE instant_date = 0 ORDER BY set_date DESC")
    fun getNotInstantDated(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE recorded = 1 ORDER BY set_date DESC")
    fun getRecorded(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set WHERE recorded = 0 ORDER BY set_date DESC")
    fun getNotRecorded(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set ORDER BY id DESC, lead_id DESC")
    fun getByLead(): Flow<List<SingleSet>>

    // TODO: create getByDate()

    @Query("SELECT * from single_set ORDER BY id DESC, set_time DESC")
    fun getBySetTime(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set ORDER BY id DESC, day_of_week DESC")
    fun getByDayOfWeek(): Flow<List<SingleSet>>

    @Query("SELECT * from single_set ORDER BY id DESC, week_number DESC")
    fun getByWeekNumber(): Flow<List<SingleSet>>

    // TODO: create histogram method for locations once those will be collected in a separate table and could be possible to choose from inserted locations
}