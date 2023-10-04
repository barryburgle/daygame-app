package com.barryburgle.gameapp.dao.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.session.AbstractSession
import java.time.LocalDate


@Dao
interface AbstractSessionDao {
    companion object {
        val abstractSessionList = mutableListOf<AbstractSession>()

        fun insertSession(abstractSession: AbstractSession) {
            // TODO: delete
            abstractSessionList.add(abstractSession)
        }

        fun selectLastSession(): List<AbstractSession> {
            // TODO: delete
            return abstractSessionList.takeLast(4)
        }
    }


    @Insert(onConflict = REPLACE)
    fun insert(abstractSession: AbstractSession)

    @Query("SELECT * from abstract_session")
    fun getAll(): List<AbstractSession>

    @Query("SELECT * from abstract_session WHERE session_date = :sessionDate")
    fun getByDate(sessionDate: LocalDate): List<AbstractSession>

    @Query("SELECT * from abstract_session ORDER BY insert_time LIMIT :limit")
    fun selectLastNSessions(limit: Int)
}