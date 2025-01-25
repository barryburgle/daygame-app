package com.barryburgle.gameapp.database.session

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.AggregatedStatDao
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.setting.Setting


@Database(
    entities = [AbstractSession::class, Setting::class],
    version = 2
)
abstract class GameAppDatabase : RoomDatabase() {
    abstract val abstractSessionDao: AbstractSessionDao
    abstract val aggregatedStatDao: AggregatedStatDao
    abstract val settingDao: SettingDao

    companion object {
        private const val DATABASE_NAME = "game_app_db"
        private var INSTANCE: GameAppDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS setting (id TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL);"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.EXPORT_FILE_NAME_ID}','daygame_export')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.IMPORT_FILE_NAME_ID}','daygame_export_yyyy_mm_dd_hh_mm.csv')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.EXPORT_FOLDER_ID}','Download')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.LAST_SESSION_AVERAGE_QUANTITY_ID}','4')"
                            + "INSERT INTO setting(id,value) VALUES ('${SettingDao.NOTIFICATION_TIME_ID}','18:00')"
                )
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "INSERT INTO setting(id,value) VALUES ('${SettingDao.IMPORT_FOLDER_ID}','Download')"
                )
            }
        }

        // TODO: check if database migration queries are executed

        fun getInstance(context: Context): GameAppDatabase? {
            if (INSTANCE == null) {
                synchronized(GameAppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GameAppDatabase::class.java, DATABASE_NAME
                    ).addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}