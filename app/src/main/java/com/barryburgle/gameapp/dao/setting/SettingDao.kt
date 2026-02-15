package com.barryburgle.gameapp.dao.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
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
        const val LAST_SESSIONS_SHOWN_ID: String = "last_sessions"
        const val LAST_WEEKS_SHOWN_ID: String = "last_weeks"
        const val LAST_MONTHS_SHOWN_ID: String = "last_months"
        const val NOTIFICATION_TIME_ID: String = "notification_time"
        const val DEFAULT_MOVING_AVERAGE_WINDOW: Int = 4
        const val DEFAULT_LAST_SESSIONS_SHOWN: Int = 14
        const val DEFAULT_LAST_WEEKS_SHOWN: Int = 8
        const val DEFAULT_LAST_MONTHS_SHOWN: Int = 4
        const val EXPORT_SESSIONS_FILE_NAME_ID: String = "export_sessions_file_name"
        const val IMPORT_SESSIONS_FILE_NAME_ID: String = "import_sessions_file_name"
        const val DEFAULT_SESSIONS_EXPORT_FILE_NAME: String = "sessions_export"
        const val DEFAULT_SESSIONS_IMPORT_FILE_NAME: String = "sessions_export_yyyy_mm_dd_hh_mm.csv"
        const val EXPORT_LEADS_FILE_NAME_ID: String = "export_leads_file_name"
        const val IMPORT_LEADS_FILE_NAME_ID: String = "import_leads_file_name"
        const val EXPORT_DATES_FILE_NAME_ID: String = "export_dates_file_name"
        const val IMPORT_DATES_FILE_NAME_ID: String = "import_dates_file_name"
        const val EXPORT_SETS_FILE_NAME_ID: String = "export_sets_file_name"
        const val IMPORT_SETS_FILE_NAME_ID: String = "import_sets_file_name"
        const val EXPORT_CHALLENGES_FILE_NAME_ID: String = "export_challenges_file_name"
        const val IMPORT_CHALLENGES_FILE_NAME_ID: String = "import_challenges_file_name"
        const val EXPORT_SETTINGS_FILE_NAME_ID: String = "export_settings_file_name"
        const val IMPORT_SETTINGS_FILE_NAME_ID: String = "import_settings_file_name"
        const val GENERATE_IDATE_ID: String = "generate_idate"
        const val FOLLOW_COUNT_ID: String = "follow_count"
        const val SUGGEST_LEADS_NATIONALITY_ID: String = "suggest_leads_nationality"
        const val AUTO_SET_EVENT_DATE_TIME_ID: String = "auto_set_event_date_time"
        const val AUTO_SET_SESSION_TIME_TO_START_ID: String = "auto_set_session_time_to_start"
        const val AUTO_SET_SET_TIME_TO_START_ID: String = "auto_set_set_time_to_start"
        const val AUTO_SET_DATE_TIME_TO_START_ID: String = "auto_set_date_time_to_start"
        const val SHOWN_NATIONALITIES_ID: String = "shown_nationalities"
        const val ARCHIVE_BACKUP_FOLDER_ID: String = "archive_backup"
        const val IS_CLEANING_ID: String = "is_cleaning"
        const val THEME_SYS_FOLLOW_ID: String = "theme_sys_follow"
        const val SIMPLE_PLUS_ONE_REPORT_ID: String = "simple_plus_one_report"
        const val NEVER_SHARE_LEAD_INFO_ID: String = "never_share_lead_info"
        const val COPY_REPORT_ON_CLIPBOARD_ID: String = "copy_report_on_clipboard"
        const val SHOW_CURRENT_WEEK_SUMMARY_ID: String = "show_current_week_summary"
        const val SHOW_CURRENT_MONTH_SUMMARY_ID: String = "show_current_month_summary"
        const val SHOW_CURRENT_CHALLENGE_SUMMARY_ID: String = "show_current_challenge_summary"
        const val INCREMENT_CHALLENGE_GOAL_ID: String = "increment_challenge_goal"
        const val DEFAULT_CHALLENGE_GOAL_ID: String = "default_challenge_goal"
        const val THEME_ID: String = "theme_id"
        const val DEFAULT_LEADS_EXPORT_FILE_NAME: String = "leads_export"
        const val DEFAULT_LEADS_IMPORT_FILE_NAME: String = "leads_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_DATES_EXPORT_FILE_NAME: String = "dates_export"
        const val DEFAULT_DATES_IMPORT_FILE_NAME: String = "dates_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_SETS_EXPORT_FILE_NAME: String = "sets_export"
        const val DEFAULT_SETS_IMPORT_FILE_NAME: String = "sets_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_CHALLENGES_EXPORT_FILE_NAME: String = "challenges_export"
        const val DEFAULT_CHALLENGES_IMPORT_FILE_NAME: String =
            "challenges_export_yyyy_mm_dd_hh_mm.csv"
        const val DEFAULT_SETTINGS_EXPORT_FILE_NAME: String = "settings_export"
        const val DEFAULT_SETTINGS_IMPORT_FILE_NAME: String =
            "settings_export_yyyy_mm_dd_hh_mm.csv"
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
        const val DEFAULT_GENERATE_IDATE_FLAG: String = "true"
        const val DEFAULT_FOLLOW_COUNT_FLAG: String = "false"
        const val DEFAULT_SUGGEST_LEADS_NATIONALITY_FLAG: String = "true"
        const val DEFAULT_AUTO_SET_EVENT_DATE_TIME_FLAG: String = "true"
        const val DEFAULT_AUTO_SET_SESSION_TIME_TO_START_FLAG: String = "true"
        const val DEFAULT_AUTO_SET_SET_TIME_TO_START_FLAG: String = "true"
        const val DEFAULT_AUTO_SET_DATE_TIME_TO_START_FLAG: String = "true"
        const val DEFAULT_SHOWN_NATIONALITIES: String = "6"
        const val DEFAULT_ARCHIVE_BACKUP_FOLDER_FLAG: String = "true"
        const val DEFAULT_IS_CLEANING_FLAG: String = "false"
        const val THEME_SYS_FOLLOW_FLAG: String = "true"
        const val SIMPLE_PLUS_ONE_REPORT_FLAG: String = "true"
        const val NEVER_SHARE_LEAD_INFO_FLAG: String = "true"
        const val COPY_REPORT_ON_CLIPBOARD_FLAG: String = "true"
        const val SHOW_CURRENT_WEEK_SUMMARY_FLAG: String = "false"
        const val SHOW_CURRENT_MONTH_SUMMARY_FLAG: String = "false"
        const val SHOW_CURRENT_CHALLENGE_SUMMARY_FLAG: String = "false"
        const val DEFAULT_INCREMENT_CHALLENGE_GOAL: Int = 5
        const val DEFAULT_CHALLENGE_GOAL: Int = 20
        const val THEME_VALUE: String = "light"
        const val DEFAULT_BACKUP_NUMBER: String = "3"
        const val DEFAULT_NOTIFICATION_TIME: String = "18:00"

        const val QUERY_LAST_SESSIONS_SHOWN =
            "SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LAST_SESSIONS_SHOWN}' ELSE value END FROM setting WHERE id = '${LAST_SESSIONS_SHOWN_ID}'"
        const val QUERY_LAST_WEEKS_SHOWN =
            "SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LAST_WEEKS_SHOWN}' ELSE value END FROM setting WHERE id = '${LAST_WEEKS_SHOWN_ID}'"
        const val QUERY_LAST_MONTHS_SHOWN =
            "SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_LAST_MONTHS_SHOWN}' ELSE value END FROM setting WHERE id = '${LAST_MONTHS_SHOWN_ID}'"

        const val QUERY_LAST_WEEKS =
            "SELECT DISTINCT strftime('%Y', meeting_date) as date_year, week_number FROM meeting UNION SELECT DISTINCT strftime('%Y', session_date) as date_year, week_number FROM abstract_session ORDER BY date_year DESC, week_number DESC LIMIT ($QUERY_LAST_WEEKS_SHOWN)"
        const val QUERY_LAST_MONTHS =
            "SELECT DISTINCT strftime('%Y', meeting_date) as date_year, strftime('%m', meeting_date) as month_number FROM meeting UNION SELECT DISTINCT strftime('%Y', session_date) as date_year, strftime('%m', session_date) as month_number FROM abstract_session ORDER BY date_year DESC, month_number DESC LIMIT ($QUERY_LAST_MONTHS_SHOWN)"
        const val QUERY_SHOWN_NATIONALITIES =
            "SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SHOWN_NATIONALITIES}' ELSE value END FROM setting WHERE id = '${SHOWN_NATIONALITIES_ID}'"
    }

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(challenges: List<Setting>)

    @Query("SELECT * from setting")
    fun getAll(): Flow<List<Setting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting)

    @Query("DELETE FROM setting")
    suspend fun deleteAll()

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

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SETS_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_SETS_FILE_NAME_ID}'")
    fun getExportSetsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SETS_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_SETS_FILE_NAME_ID}'")
    fun getImportSetsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_CHALLENGES_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_CHALLENGES_FILE_NAME_ID}'")
    fun getExportChallengesFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_CHALLENGES_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_CHALLENGES_FILE_NAME_ID}'")
    fun getImportChallengesFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SETTINGS_EXPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${EXPORT_SETTINGS_FILE_NAME_ID}'")
    fun getExportSettingsFilename(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SETTINGS_IMPORT_FILE_NAME}' ELSE value END FROM setting WHERE id = '${IMPORT_SETTINGS_FILE_NAME_ID}'")
    fun getImportSettingsFilename(): Flow<String>

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

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_GENERATE_IDATE_FLAG}' ELSE value END FROM setting WHERE id = '${GENERATE_IDATE_ID}'")
    fun getGenerateiDate(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_FOLLOW_COUNT_FLAG}' ELSE value END FROM setting WHERE id = '${FOLLOW_COUNT_ID}'")
    fun getFollowCount(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_SUGGEST_LEADS_NATIONALITY_FLAG}' ELSE value END FROM setting WHERE id = '${SUGGEST_LEADS_NATIONALITY_ID}'")
    fun getSuggestLeadsNationality(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_AUTO_SET_EVENT_DATE_TIME_FLAG}' ELSE value END FROM setting WHERE id = '${AUTO_SET_EVENT_DATE_TIME_ID}'")
    fun getAutoSetEventDateTime(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_AUTO_SET_SESSION_TIME_TO_START_FLAG}' ELSE value END FROM setting WHERE id = '${AUTO_SET_SESSION_TIME_TO_START_ID}'")
    fun getAutoSetSessionTimeToStart(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_AUTO_SET_SET_TIME_TO_START_FLAG}' ELSE value END FROM setting WHERE id = '${AUTO_SET_SET_TIME_TO_START_ID}'")
    fun getAutoSetSetTimeToStart(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_AUTO_SET_DATE_TIME_TO_START_FLAG}' ELSE value END FROM setting WHERE id = '${AUTO_SET_DATE_TIME_TO_START_ID}'")
    fun getAutoSetDateTimeToStart(): Flow<String>

    @Query(QUERY_SHOWN_NATIONALITIES)
    fun getShownNationalities(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_ARCHIVE_BACKUP_FOLDER_FLAG}' ELSE value END FROM setting WHERE id = '${ARCHIVE_BACKUP_FOLDER_ID}'")
    fun getArchiveBackupFolder(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_IS_CLEANING_FLAG}' ELSE value END FROM setting WHERE id = '${IS_CLEANING_ID}'")
    fun getIsCleaning(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${THEME_SYS_FOLLOW_FLAG}' ELSE value END FROM setting WHERE id = '${THEME_SYS_FOLLOW_ID}'")
    fun getThemeSysFollow(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${SIMPLE_PLUS_ONE_REPORT_FLAG}' ELSE value END FROM setting WHERE id = '${SIMPLE_PLUS_ONE_REPORT_ID}'")
    fun getSimplePlusOneReport(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${NEVER_SHARE_LEAD_INFO_FLAG}' ELSE value END FROM setting WHERE id = '${NEVER_SHARE_LEAD_INFO_ID}'")
    fun getNeverShareLeadInfo(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${COPY_REPORT_ON_CLIPBOARD_FLAG}' ELSE value END FROM setting WHERE id = '${COPY_REPORT_ON_CLIPBOARD_ID}'")
    fun getCopyReportOnClipboard(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${SHOW_CURRENT_WEEK_SUMMARY_FLAG}' ELSE value END FROM setting WHERE id = '${SHOW_CURRENT_WEEK_SUMMARY_ID}'")
    fun getShowCurrentWeekSummary(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${SHOW_CURRENT_MONTH_SUMMARY_FLAG}' ELSE value END FROM setting WHERE id = '${SHOW_CURRENT_MONTH_SUMMARY_ID}'")
    fun getShowCurrentMonthSummary(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${SHOW_CURRENT_CHALLENGE_SUMMARY_FLAG}' ELSE value END FROM setting WHERE id = '${SHOW_CURRENT_CHALLENGE_SUMMARY_ID}'")
    fun getShowCurrentChallengeSummary(): Flow<String>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_INCREMENT_CHALLENGE_GOAL}' ELSE value END FROM setting WHERE id = '${INCREMENT_CHALLENGE_GOAL_ID}'")
    fun getIncrementChallengeGoal(): Flow<Int>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${DEFAULT_CHALLENGE_GOAL}' ELSE value END FROM setting WHERE id = '${DEFAULT_CHALLENGE_GOAL_ID}'")
    fun getDefaultChallengeGoal(): Flow<Int>

    @Query("SELECT CASE COUNT(*) WHEN 0 THEN '${THEME_VALUE}' ELSE value END FROM setting WHERE id = '${THEME_ID}'")
    fun getTheme(): Flow<String>

    @Query(QUERY_LAST_SESSIONS_SHOWN)
    fun getLastSessionsShown(): Flow<Int>

    @Query(QUERY_LAST_WEEKS_SHOWN)
    fun getLastWeeksShown(): Flow<Int>

    @Query(QUERY_LAST_MONTHS_SHOWN)
    fun getLastMonthsShown(): Flow<Int>

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