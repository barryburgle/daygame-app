package com.barryburgle.gameapp.dao.date

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert(onConflict = REPLACE)
    suspend fun batchInsert(date: List<Date>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(date: Date): Long

    @Delete
    suspend fun delete(date: Date)

    @Query("SELECT * from meeting ORDER BY id DESC, meeting_date DESC, start_hour DESC")
    fun getAll(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY id DESC, meeting_date DESC")
    fun getByDate(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY lead_id DESC, meeting_date DESC")
    fun getByLead(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY location DESC, meeting_date DESC")
    fun getByLocation(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY start_hour DESC, meeting_date DESC")
    fun getByStartHour(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY end_hour DESC, meeting_date DESC")
    fun getByEndHour(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY cost DESC, meeting_date DESC")
    fun getByCost(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY date_number DESC, meeting_date DESC")
    fun getByDateNumber(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY date_type DESC, meeting_date DESC")
    fun getByDateType(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE pull = 1 ORDER BY meeting_date DESC")
    fun getPulled(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE pull = 0 ORDER BY meeting_date DESC")
    fun getNotPulled(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE bounce = 1 ORDER BY meeting_date DESC")
    fun getBounced(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE bounce = 0 ORDER BY meeting_date DESC")
    fun getNotBounced(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE kiss = 1 ORDER BY meeting_date DESC")
    fun getKissed(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE kiss = 0 ORDER BY meeting_date DESC")
    fun getNotKissed(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE lay = 1 ORDER BY meeting_date DESC")
    fun getLaid(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE lay = 0 ORDER BY meeting_date DESC")
    fun getNotLaid(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE recorded = 1 ORDER BY meeting_date DESC")
    fun getRecorded(): Flow<List<Date>>

    @Query("SELECT * from meeting WHERE recorded = 0 ORDER BY meeting_date DESC")
    fun getNotRecorded(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY date_time DESC, meeting_date DESC")
    fun getByDateTime(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY day_of_week DESC, meeting_date DESC")
    fun getByDayOfWeek(): Flow<List<Date>>

    @Query("SELECT * from meeting ORDER BY week_number DESC, meeting_date DESC")
    fun getByWeekNumber(): Flow<List<Date>>

    @Query("SELECT nationality as category, COUNT(*) as frequency FROM lead where lead.id IN (SELECT DISTINCT(lead_id) FROM meeting) GROUP BY nationality ORDER BY nationality")
    fun getNationalityHistogram(): Flow<List<CategoryHistogram>>

    @Query("SELECT age as metric, COUNT(*) as frequency FROM lead where lead.id IN (SELECT DISTINCT(lead_id) FROM meeting) GROUP BY age ORDER BY age")
    fun getAgeHistogram(): Flow<List<Histogram>>

    @Query("SELECT date_number as metric, COUNT(*) as frequency FROM meeting GROUP BY date_number ORDER BY date_number")
    fun getNumberHistogram(): Flow<List<Histogram>>
}