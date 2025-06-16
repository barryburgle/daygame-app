package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFC6B084)
private val ContainerColor = Color(0xFFB0102D)
private val InterestColor = Color(0xFFFFFCFF)
private val BackgroundColor = Color(0xFFDCCCAC)
private val SelectedColor = Color(0xFF9D8148)
private val TextColor = Color(0xFF13100C)

val CrownColorPalette = lightColorScheme(
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

val CrownColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)