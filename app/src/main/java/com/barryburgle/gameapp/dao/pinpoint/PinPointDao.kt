package com.barryburgle.gameapp.dao.pinpoint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.session.PinPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface PinPointDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(pinpoint: PinPoint): Long

    @Delete
    suspend fun delete(pinpoint: PinPoint)

    @Query("DELETE FROM pinpoint")
    suspend fun deleteAll()

    @Query("SELECT * from pinpoint ORDER BY id DESC, utc_timestamp DESC")
    fun getAll(): Flow<List<PinPoint>>

    @Query("SELECT * FROM pinpoint WHERE session_id=:sessionId")
    fun getBySessionId(sessionId: Long): Flow<PinPoint>

    @Query("SELECT * from pinpoint WHERE pinpoint_type IN ('set', 'conversation', 'contact') ORDER BY id DESC, utc_timestamp DESC")
    fun getSets(): Flow<List<PinPoint>>

    @Query("SELECT * from pinpoint WHERE pinpoint_type IN ('conversation', 'contact') ORDER BY id DESC, utc_timestamp DESC")
    fun getConversations(): Flow<List<PinPoint>>

    @Query("SELECT * from pinpoint WHERE pinpoint_type='contact'  ORDER BY id DESC, utc_timestamp DESC")
    fun getContacts(): Flow<List<PinPoint>>
}