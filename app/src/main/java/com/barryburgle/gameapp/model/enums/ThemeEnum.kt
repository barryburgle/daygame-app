package com.barryburgle.gameapp.model.enums

import androidx.compose.material3.ColorScheme
import com.barryburgle.gameapp.ui.theme.palette.*

enum class ThemeEnum(val type: String) {
    DARK("dark"),
    LIGHT("light"),
    INFINITE("infinite"),
    MASTERY("mastery"),
    HUSTLE("hustle");

    companion object {
        fun getTheme(type: String): ColorScheme {
            return when (type) {
                DARK.type -> DarkColorPalette
                LIGHT.type -> LightColorPalette
                INFINITE.type -> InfiniteColorPalette
                MASTERY.type -> MasteryColorScheme
                HUSTLE.type -> HustleColorPalette
                else -> LightColorPalette
            }
        }
    }
}