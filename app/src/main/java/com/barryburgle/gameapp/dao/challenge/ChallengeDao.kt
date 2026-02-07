package com.barryburgle.gameapp.dao.challenge

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    companion object {
        const val ACHIEVED_CHALLENGE_QUERY = """
        SELECT *, (
            CASE challenge.challenge_type
                WHEN 'set' THEN (SELECT IFNULL(SUM(sets), 0) FROM abstract_session WHERE session_date >= challenge.start_date AND session_date <= challenge.end_date)
                WHEN 'conversation' THEN (SELECT IFNULL(SUM(convos), 0) FROM abstract_session WHERE session_date >= challenge.start_date AND session_date <= challenge.end_date)
                WHEN 'contact' THEN (SELECT IFNULL(SUM(contacts), 0) FROM abstract_session WHERE session_date >= challenge.start_date AND session_date <= challenge.end_date)
                ELSE 0
            END
        ) as achieved from challenge
        """
    }

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(challenges: List<Challenge>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(challenge: Challenge): Long

    @Delete
    suspend fun delete(challenge: Challenge)

    @Query("DELETE FROM challenge")
    suspend fun deleteAll()

    @Query("${ACHIEVED_CHALLENGE_QUERY} ORDER BY insert_time DESC")
    fun getAll(): Flow<List<AchievedChallenge>>

    @Query("SELECT challenge_type as category, COUNT(*) as frequency from challenge GROUP BY challenge_type ORDER BY challenge_type")
    fun getTypeHistogram(): Flow<List<CategoryHistogram>>

    @Query("SELECT CAST(JULIANDAY(end_date) - JULIANDAY(start_date) AS INTEGER) as metric, COUNT(*) as frequency from challenge GROUP BY metric ORDER BY metric")
    fun getDurationHistogram(): Flow<List<Histogram>>

    @Query("${ACHIEVED_CHALLENGE_QUERY} ORDER BY id DESC, start_date DESC")
    fun getByStartDate(): Flow<List<AchievedChallenge>>

    @Query("${ACHIEVED_CHALLENGE_QUERY} ORDER BY id DESC, end_date DESC")
    fun getByEndDate(): Flow<List<AchievedChallenge>>

    @Query("${ACHIEVED_CHALLENGE_QUERY} ORDER BY id DESC, challenge_type DESC")
    fun getByChallengeType(): Flow<List<AchievedChallenge>>

    @Query("${ACHIEVED_CHALLENGE_QUERY} ORDER BY id DESC, challenge_type DESC, goal DESC")
    fun getByChallengeTypeAndGoal(): Flow<List<AchievedChallenge>>
}