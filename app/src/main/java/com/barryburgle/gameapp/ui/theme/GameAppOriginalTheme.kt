package com.barryburgle.gameapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Grey05,
    onPrimary = Grey75,
    primaryContainer = Grey20,
    onPrimaryContainer = Color.White,
    inversePrimary = Grey05,
    secondary = Color.White,
    secondaryContainer = Grey20,
    onErrorContainer = Red30,
    background = Grey10,
    surface = Grey05,
    onSurface = Color.White,
    surfaceVariant = Grey10,
    onSurfaceVariant = Color.White,
    outline = Color.White,
    tertiary = Grey50,
    onTertiary = Grey50
)

private val LightColorPalette = lightColorScheme(
    primary = Grey85,
    onPrimary = Color.Black,
    primaryContainer = Grey65,
    onPrimaryContainer = Color.Black,
    inversePrimary = Grey95,
    secondary = Color.Black,
    secondaryContainer = Grey70,
    onErrorContainer = Red30,
    background = Color.White,
    surface = Grey95,
    onSurface = Color.Black,
    surfaceVariant = Grey65,
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    tertiary = Grey50,
    onTertiary = Grey30
)

@Composable
fun GameAppOriginalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}