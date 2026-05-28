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

    // TODO: fun getBySessionId(..)
}