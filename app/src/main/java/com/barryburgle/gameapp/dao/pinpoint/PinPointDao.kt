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
    suspend fun batchInsert(pinpoints: List<PinPoint>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(pinpoint: PinPoint): Long

    @Delete
    suspend fun delete(pinpoint: PinPoint)

    @Query("UPDATE pinpoint SET pinpoint_type = :newPinpointType WHERE id = (SELECT id FROM pinpoint WHERE source_event_id = :sourceEventId AND source_event_type = \"set\" ORDER BY id DESC LIMIT 1)")
    suspend fun updatePinpointTypeForSetPinPoint(
        sourceEventId: Long,
        newPinpointType: String
    )

    @Query("DELETE FROM pinpoint WHERE id in (SELECT id FROM pinpoint WHERE source_event_id = :sourceEventId AND source_event_type = :sourceEventType)")
    suspend fun deleteAllBySourceEventIdAndSourceEventType(
        sourceEventId: Long, sourceEventType: String
    )

    @Query("DELETE FROM pinpoint WHERE id = (SELECT id FROM pinpoint WHERE source_event_id = :sourceEventId AND source_event_type = :sourceEventType AND pinpoint_type = :pinpointType ORDER BY id DESC LIMIT 1)")
    suspend fun deleteLastPinPointBySourceEventIdAndSourceEventTypeAndType(
        sourceEventId: Long, sourceEventType: String, pinpointType: String
    )

    @Query("SELECT id FROM pinpoint WHERE pinpoint_type = 'contact' ORDER BY id DESC LIMIT 1")
    suspend fun getLastContactPinPointId() : Long?

    @Query("DELETE FROM pinpoint")
    suspend fun deleteAll()

    @Query("SELECT * from pinpoint ORDER BY id DESC, local_timestamp DESC")
    fun getAll(): Flow<List<PinPoint>>

    @Query("SELECT * FROM pinpoint WHERE source_event_id=:sourceEventId AND source_event_type=:sourceEventType ORDER BY id DESC LIMIT 1")
    fun getBySourceEventIdAndSourceEventType(
        sourceEventId: Long, sourceEventType: String
    ): Flow<PinPoint>

    @Query("SELECT * from pinpoint WHERE pinpoint_type IN ('set', 'conversation', 'contact') ORDER BY id DESC, local_timestamp DESC")
    fun getSets(): Flow<List<PinPoint>>

    @Query("SELECT * from pinpoint WHERE pinpoint_type IN ('conversation', 'contact') ORDER BY id DESC, local_timestamp DESC")
    fun getConversations(): Flow<List<PinPoint>>

    @Query("SELECT * from pinpoint WHERE pinpoint_type='contact'  ORDER BY id DESC, local_timestamp DESC")
    fun getContacts(): Flow<List<PinPoint>>
}