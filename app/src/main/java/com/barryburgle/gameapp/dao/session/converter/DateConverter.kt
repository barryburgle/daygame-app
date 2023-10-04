package com.barryburgle.gameapp.dao.session.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

class DateConverter {

    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun instantFromTimestamp(instant: Long): Instant? {
        return Instant.ofEpochMilli(instant)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return instantToTimestamp(date?.toInstant())
    }

    @TypeConverter
    fun dateFromTimestamp(date: Long): Date? {
        return Date.from(Instant.ofEpochMilli(date))
    }

    @TypeConverter
    fun localDateToTimestamp(localDate: LocalDate?): Long? {
        return dateToTimestamp(
            Date.from(
                localDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            )
        )
    }

    @RequiresApi(34)
    @TypeConverter
    fun localDateFromTimestamp(localDate: Long): LocalDate? {
        return LocalDate.ofInstant(instantFromTimestamp(localDate), ZoneId.systemDefault())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @TypeConverter
    fun localTimeToTimestamp(localTime: LocalTime?): Long? {
        return localTime?.toEpochSecond(LocalDate.now(), ZoneOffset.UTC)
    }

    @TypeConverter
    fun localTimeFromTimestamp(localTime: Long): LocalTime? {
        return LocalTime.from(instantFromTimestamp(localTime))
    }
}