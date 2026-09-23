package com.barryburgle.gameapp.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromLongList(value: List<Long>?): String {
        return value?.joinToString(separator = ",") ?: ""
    }

    @TypeConverter
    fun toLongList(value: String?): List<Long> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(",").mapNotNull { it.toLongOrNull() }
    }
}