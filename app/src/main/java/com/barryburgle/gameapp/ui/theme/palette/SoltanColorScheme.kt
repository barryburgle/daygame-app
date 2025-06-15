package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val InterestColor = Color(0xFF05147B)
private val ContainerColor = Color(0xFF071DB0)
private val SelectedColor = Color(0xFFFFFF01)
private val TappableColor = Color(0xFFFFFF4D)
private val BackgroundColor = Color(0xFF030C4A)
private val TextColor = Color(0xFF7F8000)

val SoltanColorPalette = lightColorScheme(
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

val SoltanColorScheme = ColorPaletteHint(InterestColor, TextColor, BackgroundColor)