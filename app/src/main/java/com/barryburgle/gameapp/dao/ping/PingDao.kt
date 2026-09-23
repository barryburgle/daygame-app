package com.barryburgle.gameapp.dao.ping

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.ping.Ping
import kotlinx.coroutines.flow.Flow

@Dao
interface PingDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(ping: Ping): Long

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(pings: List<Ping>)

    @Query("SELECT * FROM ping ORDER BY id DESC")
    fun getAll(): Flow<List<Ping>>

    @Query("DELETE FROM ping WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ping")
    suspend fun deleteAll()
}