package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val InterestColor = Color(0xFF171E4F)
private val ContainerColor = Color(0xFF2555c1)
private val SelectedColor = Color(0xFF42B4C7)
private val TappableColor = Color(0xFF4A504A)
private val BackgroundColor = Color(0xFF857154)
private val TextColor = Color(0xFFC2BFAC)

val InfiniteColorPalette = lightColorScheme(
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

val InfiniteColorPaletteHint = ColorPaletteHint(InterestColor, BackgroundColor)