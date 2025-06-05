package com.barryburgle.gameapp.model.enums

import androidx.compose.material3.ColorScheme
import com.barryburgle.gameapp.ui.theme.DarkColorPalette
import com.barryburgle.gameapp.ui.theme.LightColorPalette

enum class ThemeEnum(val type: String) {
    DARK("dark"),
    LIGHT("light");

    // TODO: ADD OTHER THEMES

    companion object {
        fun getTheme(type: String): ColorScheme {
            return when (type) {
                DARK.type -> DarkColorPalette
                LIGHT.type -> LightColorPalette
                else -> LightColorPalette
            }
        }
    }
}