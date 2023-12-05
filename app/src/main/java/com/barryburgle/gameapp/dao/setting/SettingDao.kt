package com.barryburgle.gameapp.dao.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barryburgle.gameapp.model.setting.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    companion object {
        const val EXPORT_FILE_NAME_ID: String = "export_file_name"
        const val IMPORT_FILE_NAME_ID: String = "import_file_name"
        const val EXPORT_FOLDER_ID: String = "export_folder"
        const val LAST_SESSION_AVERAGE_QUANTITY_ID: String = "average_last"
        const val NOTIFICATION_TIME_ID: String = "notification_time"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting)

    @Query("SELECT * FROM setting WHERE id=:id")
    fun getById(id: String): Flow<Setting>

    @Query("SELECT COALESCE(value,'daygame_export') FROM setting WHERE id='${EXPORT_FILE_NAME_ID}'")
    fun getExportFilename(): Flow<String>

    @Query("SELECT COALESCE(value,'daygame_export_yyyy_mm_dd_hh_mm.csv') FROM setting WHERE id='${IMPORT_FILE_NAME_ID}'")
    fun getImportFilename(): Flow<String>

    @Query("SELECT COALESCE(value,'Download') FROM setting WHERE id='${EXPORT_FOLDER_ID}'")
    fun getExportFolder(): Flow<String>

    @Query("SELECT COALESCE(value,4) FROM setting WHERE id='${LAST_SESSION_AVERAGE_QUANTITY_ID}'")
    fun getAverageLast(): Flow<Int>

    @Query("SELECT COALESCE(value,'18:00') FROM setting WHERE id='${NOTIFICATION_TIME_ID}'")
    fun getNotificationTime(): Flow<String>
}