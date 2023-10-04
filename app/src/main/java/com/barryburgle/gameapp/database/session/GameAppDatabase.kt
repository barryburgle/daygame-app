package com.barryburgle.gameapp.database.session

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.dao.session.converter.DateConverter
import com.barryburgle.gameapp.model.session.AbstractSession

@Database(entities = arrayOf(AbstractSession::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class GameAppDatabase : RoomDatabase() {
    abstract fun abstractSessionDao(): AbstractSessionDao

    companion object {
        private var INSTANCE: GameAppDatabase? = null

        fun getInstance(context: Context): GameAppDatabase? {
            if (INSTANCE == null) {
                synchronized(GameAppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        GameAppDatabase::class.java, "game_app_db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}