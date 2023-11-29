package com.barryburgle.gameapp.dao.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barryburgle.gameapp.model.setting.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting)

    @Query("SELECT * FROM setting WHERE id=:id")
    fun getById(id: String): Flow<Setting>

    @Query("SELECT COALESCE(value,4) FROM setting WHERE id='average_last'")
    fun getAverageLast(): Flow<Int>
}