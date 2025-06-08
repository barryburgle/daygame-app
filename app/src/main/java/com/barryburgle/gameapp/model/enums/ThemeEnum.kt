package com.barryburgle.gameapp.model.enums

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.palette.*

enum class ThemeEnum(val type: String) {
    DARK("dark"),
    LIGHT("light"),
    INFINITE("infinite"),
    MASTERY("mastery"),
    HUSTLE("hustle"),
    STEALTH("stealth");

    companion object {
        fun getTheme(type: String): ColorScheme {
            return when (type) {
                DARK.type -> DarkColorPalette
                LIGHT.type -> LightColorPalette
                INFINITE.type -> InfiniteColorPalette
                MASTERY.type -> MasteryColorScheme
                HUSTLE.type -> HustleColorPalette
                STEALTH.type -> StealthColorPalette
                else -> LightColorPalette
            }
        }

        fun sortedValues(): List<ThemeEnum> {
            return ThemeEnum.values().sortedBy { it.type }
        }
    }

    fun getFirstHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.firstColor
            LIGHT.type -> LightColorPaletteHint.firstColor
            INFINITE.type -> InfiniteColorPaletteHint.firstColor
            MASTERY.type -> MasteryColorPaletteHint.firstColor
            HUSTLE.type -> HustleColorPaletteHint.firstColor
            STEALTH.type -> StealthColorPaletteHint.firstColor
            else -> LightColorPaletteHint.firstColor
        }
    }

    fun getSecondHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.secondColor
            LIGHT.type -> LightColorPaletteHint.secondColor
            INFINITE.type -> InfiniteColorPaletteHint.secondColor
            MASTERY.type -> MasteryColorPaletteHint.secondColor
            HUSTLE.type -> HustleColorPaletteHint.secondColor
            STEALTH.type -> StealthColorPaletteHint.secondColor
            else -> LightColorPaletteHint.secondColor
        }
    }
}