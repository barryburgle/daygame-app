package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFF655D52)
private val ContainerColor = Color(0xFF0E0814)
private val InterestColor = Color(0xFF332B3A)
private val BackgroundColor = Color(0xFFD5BB87)
private val SelectedColor = Color(0xFFF33D07)
private val TextColor = Color(0xFFF6E6BF)

val NorthColorPalette = lightColorScheme(
    primary = InterestColor,
    onPrimary = TextColor,
    primaryContainer = TappableColor,
    onPrimaryContainer = TextColor,
    inversePrimary = InterestColor,
    secondary = TextColor,
    secondaryContainer = TextColor,
    onErrorContainer = Red30,
    background = BackgroundColor,
    surface = ContainerColor,
    onSurface = TextColor,
    surfaceVariant = TappableColor,
    onSurfaceVariant = SelectedColor,
    outline = TextColor,
    tertiary = TextColor,
    onTertiary = BackgroundColor
)

val NorthColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)