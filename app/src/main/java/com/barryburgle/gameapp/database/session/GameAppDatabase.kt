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
                database.execSQL("CREATE TABLE IF NOT EXISTS setting (id TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL)")
            }
        }

        fun getInstance(context: Context): GameAppDatabase? {
            if (INSTANCE == null) {
                synchronized(GameAppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GameAppDatabase::class.java, DATABASE_NAME
                    ).addMigrations(MIGRATION_1_2).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}