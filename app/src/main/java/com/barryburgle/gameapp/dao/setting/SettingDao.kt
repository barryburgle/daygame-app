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
        const val LATEST_AVAILABLE_ID: String = "latest_available"
        const val LATEST_PUBLISH_DATE_ID: String = "latest_publish_date"
        const val LATEST_CHANGELOG_ID: String = "latest_changelog"
        const val LATEST_DOWNLOAD_URL_ID: String = "latest_download_url"
        const val EXPORT_FOLDER_ID: String = "export_folder"
        const val IMPORT_FOLDER_ID: String = "import_folder"
        const val BACKUP_FOLDER_ID: String = "backup_folder"
        const val EXPORT_HEADER_ID: String = "export_header"
        const val IMPORT_HEADER_ID: String = "import_header"
        const val BACKUP_ACTIVE_ID: String = "backup_active"
        const val BACKUP_NUMBER_ID: String = "backup_number"
        const val LAST_SESSION_AVERAGE_QUANTITY_ID: String = "average_last"
        const val NOTIFICATION_TIME_ID: String = "notification_time"
        const val DEFAULT_MOVING_AVERAGE_WINDOW: Int = 4
        const val EXPORT_SESSIONS_FILE_NAME_ID: String = "export_sessions_file_name"
        const val IMPORT_SESSIONS_FILE_NAME_ID: String = "import_sessions_file_name"
        const val DEFAULT_SESSIONS_EXPORT_FILE_NAME: String = "sessions_export"
        const val DEFAULT_SESSIONS_IMPORT_FILE_NAME: String = "sessions_export_yyyy_mm_dd_hh_mm.csv"
        const val EXPORT_LEADS_FILE_NAME_ID: String = "export_leads_file_name"
        const val IMPORT_LEADS_FILE_NAME_ID: String = "import_leads_file_name"
        const val EXPORT_DATES_FILE_NAME_ID: String = "export_dates_file_name"
        const val IMPORT_DATES_FILE_NAME_ID: String = "import_dates_file_name"
        const val DEFAULT_LEADS_EXPORT_FILE_NAME: String = "leads_export"
        const val DEFAULT_LEADS_IMPORT_FILE_NAME: String = "leads_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_DATES_EXPORT_FILE_NAME: String = "date_export"
        const val DEFAULT_DATES_IMPORT_FILE_NAME: String = "date_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_LATEST_AVAILABLE: String = ""
        const val DEFAULT_LATEST_PUBLISH_DATE: String = ""
        const val DEFAULT_LATEST_CHANGELOG: String = ""
        const val DEFAULT_LATEST_DOWNLOAD_URL: String = ""
        const val DEFAULT_EXPORT_FOLDER: String = "Download"
        const val DEFAULT_IMPORT_FOLDER: String = "Download"
        const val DEFAULT_BACKUP_FOLDER: String = "Backup_Daygame"
        const val DEFAULT_EXPORT_HEADER_FLAG: String = "false"
        const val DEFAULT_IMPORT_HEADER_FLAG: String = "false"
        const val DEFAULT_BACKUP_ACTIVE_FLAG: String = "true"
        const val DEFAULT_BACKUP_NUMBER: String = "3"
        const val DEFAULT_NOTIFICATION_TIME: String = "18:00"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting)

    @Query("SELECT * FROM setting WHERE id=:id")
    fun getById(id: String): Flow<Setting>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SESSIONS_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_SESSIONS_FILE_NAME_ID}'")
    fun getExportSessionsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SESSIONS_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_SESSIONS_FILE_NAME_ID}'")
    fun getImportSessionsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LEADS_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_LEADS_FILE_NAME_ID}'")
    fun getExportLeadsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LEADS_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_LEADS_FILE_NAME_ID}'")
    fun getImportLeadsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_DATES_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_DATES_FILE_NAME_ID}'")
    fun getExportDatesFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_DATES_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_DATES_FILE_NAME_ID}'")
    fun getImportDatesFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_EXPORT_FOLDER}' ELSE value END FROM setting WHERE id = '${EXPORT_FOLDER_ID}'")
    fun getExportFolder(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_IMPORT_FOLDER}' ELSE value END FROM setting WHERE id = '${IMPORT_FOLDER_ID}'")
    fun getImportFolder(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_BACKUP_FOLDER}' ELSE value END FROM setting WHERE id = '${BACKUP_FOLDER_ID}'")
    fun getBackupFolder(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_EXPORT_HEADER_FLAG}' ELSE value END FROM setting WHERE id = '${EXPORT_HEADER_ID}'")
    fun getExportHeaderFlag(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_IMPORT_HEADER_FLAG}' ELSE value END FROM setting WHERE id = '${IMPORT_HEADER_ID}'")
    fun getImportHeaderFlag(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_BACKUP_ACTIVE_FLAG}' ELSE value END FROM setting WHERE id = '${BACKUP_ACTIVE_ID}'")
    fun getBackupActiveFlag(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_BACKUP_NUMBER}' ELSE value END FROM setting WHERE id = '${BACKUP_NUMBER_ID}'")
    fun getBackupNumber(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_MOVING_AVERAGE_WINDOW}' ELSE value END FROM setting WHERE id = '${LAST_SESSION_AVERAGE_QUANTITY_ID}'")
    fun getAverageLast(): Flow<Int>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_NOTIFICATION_TIME}' ELSE value END FROM setting WHERE id = '${NOTIFICATION_TIME_ID}'")
    fun getNotificationTime(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LATEST_AVAILABLE}' ELSE value END FROM setting WHERE id = '${LATEST_AVAILABLE_ID}'")
    fun getLatestAvailable(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LATEST_PUBLISH_DATE}' ELSE value END FROM setting WHERE id = '${LATEST_PUBLISH_DATE_ID}'")
    fun getLatestPublishDate(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LATEST_CHANGELOG}' ELSE value END FROM setting WHERE id = '${LATEST_CHANGELOG_ID}'")
    fun getLatestChangelog(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LATEST_DOWNLOAD_URL}' ELSE value END FROM setting WHERE id = '${LATEST_DOWNLOAD_URL_ID}'")
    fun getLatestDownloadUrl(): Flow<String>
}