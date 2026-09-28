package com.barryburgle.gameapp.dao.date

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.date.DateModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DateModelDao {

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(dateModels: List<DateModel>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(dateModel: DateModel): Long

    // TODO: refactor all delete methods and understand if better to delete by object id or by object
    @Query("DELETE FROM date_model WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM date_phase")
    suspend fun deleteAll()

    @Query("SELECT * from date_model ORDER BY id DESC")
    fun getAll(): Flow<List<DateModel>>

    @Query(
        """
        UPDATE date_model 
        SET phases = CASE 
            WHEN phases IS NULL OR phases = '' THEN ',' || :datePhaseId || ','
            ELSE phases || :datePhaseId || ','
        END 
        WHERE id = :dateModelId
        """
    )
    suspend fun addDatePhaseToModel(dateModelId: Long, datePhaseId: Long)
}