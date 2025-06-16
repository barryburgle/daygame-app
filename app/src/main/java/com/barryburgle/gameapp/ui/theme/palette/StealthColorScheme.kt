package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFF00374D)
private val ContainerColor = Color(0xFF002241)
private val InterestColor = Color(0xFF005C80)
private val BackgroundColor = Color(0xFF748EA5)
private val SelectedColor = Color(0xFF011323)
private val TextColor = Color(0xFFFFFEFE)

val StealthColorPalette = lightColorScheme(
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

val StealthColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)