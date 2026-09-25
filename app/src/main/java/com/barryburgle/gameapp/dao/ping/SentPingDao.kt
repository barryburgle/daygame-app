package com.barryburgle.gameapp.dao.ping

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.ping.SentPing
import kotlinx.coroutines.flow.Flow

@Dao
interface SentPingDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(sentPing: SentPing): Long

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(sentPings: List<SentPing>)

    @Query("SELECT * FROM sent_ping ORDER BY id DESC")
    fun getAll(): Flow<List<SentPing>>

    @Query("DELETE FROM sent_ping")
    suspend fun deleteAll()
}