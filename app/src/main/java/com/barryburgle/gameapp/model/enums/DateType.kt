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
    }
}