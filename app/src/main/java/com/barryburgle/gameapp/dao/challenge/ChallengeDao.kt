package com.barryburgle.gameapp.dao.challenge

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(challenge: Challenge): Long

    @Delete
    suspend fun delete(challenge: Challenge)

    @Query("DELETE FROM challenge")
    suspend fun deleteAll()

    @Query("SELECT * from challenge ORDER BY insert_time DESC")
    fun getAll(): Flow<List<Challenge>>

    @Query("SELECT challenge_type as category, COUNT(*) as frequency from challenge GROUP BY challenge_type ORDER BY challenge_type")
    fun getTypeHistogram(): Flow<List<CategoryHistogram>>

    @Query("SELECT CAST(JULIANDAY(end_date) - JULIANDAY(start_date) AS INTEGER) as metric, COUNT(*) as frequency from challenge GROUP BY metric ORDER BY metric")
    fun getDurationHistogram(): Flow<List<Histogram>>

    @Query("SELECT * from challenge ORDER BY id DESC, start_date DESC")
    fun getByStartDate(): Flow<List<Challenge>>

    @Query("SELECT * from challenge ORDER BY id DESC, end_date DESC")
    fun getByEndDate(): Flow<List<Challenge>>

    @Query("SELECT * from challenge ORDER BY id DESC, challenge_type DESC")
    fun getByChallengeType(): Flow<List<Challenge>>

    @Query("SELECT * from challenge ORDER BY id DESC, challenge_type DESC, goal DESC")
    fun getByChallengeTypeAndGoal(): Flow<List<Challenge>>
}