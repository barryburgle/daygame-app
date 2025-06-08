package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Grey05
import com.barryburgle.gameapp.ui.theme.Grey10
import com.barryburgle.gameapp.ui.theme.Grey20
import com.barryburgle.gameapp.ui.theme.Grey50
import com.barryburgle.gameapp.ui.theme.Grey75
import com.barryburgle.gameapp.ui.theme.Red30

val DarkColorPalette = darkColorScheme(
    primary = Grey05,
    onPrimary = Grey75,
    primaryContainer = Grey20,
    onPrimaryContainer = Color.White,
    inversePrimary = Grey20,
    secondary = Grey75,
    secondaryContainer = Grey50,
    onErrorContainer = Red30,
    background = Grey10,
    surface = Grey05,
    onSurface = Grey75,
    surfaceVariant = Grey10,
    onSurfaceVariant = Grey75,
    outline = Grey75,
    tertiary = Grey50,
    onTertiary = Grey50
)

val DarkColorPaletteHint = ColorPaletteHint(Grey10, Grey20, Grey50)