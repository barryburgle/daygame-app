package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.session.AbstractSession
import kotlinx.coroutines.flow.Flow


@Dao
interface AbstractSessionDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(abstractSession: AbstractSession)

    @Delete
    suspend fun delete(abstractSession: AbstractSession)

    @Query("SELECT * from abstract_session")
    fun getAll(): Flow<List<AbstractSession>>

    @Query("SELECT * from (SELECT * from abstract_session ORDER BY id DESC LIMIT :limit) ORDER BY id ASC")
    fun getAllLimit(limit: Int): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY session_date DESC, end_hour DESC")
    fun getByDate(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY sets DESC")
    fun getBySets(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY convos DESC")
    fun getByConvos(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY contacts DESC")
    fun getByContacts(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY session_time")
    fun getBySessionTime(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY approach_time")
    fun getByApproachTime(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY convo_ratio DESC")
    fun getByConvoRatio(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY rejection_ratio DESC")
    fun getByRejectionRatio(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY contact_ratio DESC")
    fun getByContactRatio(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY `index` DESC")
    fun getByIndex(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY day_of_week")
    fun getByDayOfWeek(): Flow<List<AbstractSession>>

    @Query("SELECT * from abstract_session ORDER BY week_number")
    fun getByWeekNumber(): Flow<List<AbstractSession>>

}