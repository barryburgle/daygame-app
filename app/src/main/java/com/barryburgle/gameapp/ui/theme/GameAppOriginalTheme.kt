package com.barryburgle.gameapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.ui.theme.palette.DarkColorPalette
import com.barryburgle.gameapp.ui.theme.palette.LightColorPalette

@Composable
fun GameAppOriginalTheme(
    theme: String = ThemeEnum.LIGHT.type,
    themeSysFollow: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (themeSysFollow) {
        if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    } else {
        ThemeEnum.getTheme(theme)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}