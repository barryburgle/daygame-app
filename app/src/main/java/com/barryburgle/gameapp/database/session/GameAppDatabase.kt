package com.barryburgle.gameapp.database.session

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.barryburgle.gameapp.dao.date.DateDao
import com.barryburgle.gameapp.dao.lead.LeadDao
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedDatesDao
import com.barryburgle.gameapp.dao.session.AggregatedSessionsDao
import com.barryburgle.gameapp.dao.set.SetDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.setting.Setting

@Database(
    entities = [AbstractSession::class, Setting::class, Lead::class, Date::class, SingleSet::class],
    version = 5
)
abstract class GameAppDatabase : RoomDatabase() {
    abstract val abstractSessionDao: AbstractSessionDao
    abstract val aggregatedSessionsDao: AggregatedSessionsDao
    abstract val aggregatedDatesDao: AggregatedDatesDao
    abstract val settingDao: SettingDao
    abstract val leadDao: LeadDao
    abstract val dateDao: DateDao
    abstract val setDao: SetDao

    companion object {
        private const val DATABASE_NAME = "game_app_db"
        private var INSTANCE: GameAppDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS setting (id TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL);"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.EXPORT_SESSIONS_FILE_NAME_ID}','daygame_export')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.IMPORT_SESSIONS_FILE_NAME_ID}','daygame_export_yyyy_mm_dd_hh_mm.csv')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.EXPORT_FOLDER_ID}','Download')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.LAST_SESSION_AVERAGE_QUANTITY_ID}','4')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.NOTIFICATION_TIME_ID}','18:00')"
                )
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS lead (id INTEGER NOT NULL PRIMARY KEY, insert_time TEXT NOT NULL, session_id INTEGER NULL, name TEXT NULL, contact TEXT NULL, nationality TEXT NULL, age INTEGER NOT NULL);"
                )
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS meeting (id INTEGER NOT NULL PRIMARY KEY, insert_time TEXT NULL, lead_id INTEGER NULL, location TEXT NULL, meeting_date TEXT NULL, start_hour TEXT NULL, end_hour TEXT NULL, cost INTEGER NULL, date_number INTEGER NOT NULL, date_type TEXT NOT NULL, pull INTEGER NOT NULL, bounce INTEGER NOT NULL, kiss INTEGER NOT NULL, lay INTEGER NOT NULL, recorded INTEGER NOT NULL, sticking_points TEXT NULL, tweet_url TEXT NULL, date_time INTEGER NULL, day_of_week INTEGER NULL, week_number INTEGER NULL);"
                )
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS single_set (id INTEGER NOT NULL PRIMARY KEY, insert_time TEXT NULL, start_hour TEXT NULL, end_hour TEXT NULL, set_date TEXT NULL, session_id INTEGER NULL, location TEXT NULL, conversation INTEGER NOT NULL, contact INTEGER NOT NULL, instant_date INTEGER NOT NULL, recorded INTEGER NOT NULL, lead_id INTEGER NULL, date_id INTEGER NULL, sticking_points TEXT NULL, tweet_url TEXT NULL, set_time INTEGER NULL, day_of_week INTEGER NULL, week_number INTEGER NULL);"
                )
            }
        }

        fun getInstance(context: Context): GameAppDatabase? {
            if (INSTANCE == null) {
                synchronized(GameAppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GameAppDatabase::class.java, DATABASE_NAME
                    ).addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4).addMigrations(MIGRATION_4_5).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}