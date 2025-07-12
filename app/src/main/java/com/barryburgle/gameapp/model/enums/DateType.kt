package com.barryburgle.gameapp.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Panorama
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.ui.graphics.vector.ImageVector

enum class DateType(private val type: String) {
    // TODO: rename enum to DateTypeEnum
    DRINK("drink"),
    COFFEE("coffee"),
    WALK("walk"),
    FOOD("food"),
    EXPERIENCE("experience");

    fun getType(): String {
        return type
    }

    companion object {
        fun getIcon(type: String): ImageVector {
            return when (type) {
                DRINK.getType() -> Icons.Default.WineBar
                COFFEE.getType() -> Icons.Default.Coffee
                WALK.getType() -> Icons.AutoMirrored.Filled.DirectionsWalk
                FOOD.getType() -> Icons.Default.Restaurant
                EXPERIENCE.getType() -> Icons.Default.Panorama
                else -> Icons.Default.Favorite
            }
        }

        fun getEmoji(type: String): String {
            return when (type) {
                DRINK.getType() -> "\uD83C\uDF78"
                COFFEE.getType() -> "☕"
                WALK.getType() -> "\uD83D\uDEB6\uD83D\uDEB6"
                FOOD.getType() -> "\uD83C\uDF5C"
                EXPERIENCE.getType() -> "\uD83C\uDFDE\uFE0F"
                else -> "❤"
            }
        }

        fun getDateNumber(dateNumber: Int, writeDate: Boolean): String {
            // TODO: move to its own class in future if needed
            var dateNumberSuffix = "th"
            var dateNumberCount = dateNumber.toString()
            if (dateNumber == 0) {
                return "iDate"
            } else if (dateNumber == 1) {
                dateNumberSuffix = "st"
            } else if (dateNumber == 2) {
                dateNumberSuffix = "nd"
            } else if (dateNumber == 3) {
                dateNumberSuffix = "rd"
            }
            var dateDescription = dateNumberCount + dateNumberSuffix
            if (writeDate) {
                dateDescription = dateDescription + "date"
            }
            return dateDescription
        }
    }
}