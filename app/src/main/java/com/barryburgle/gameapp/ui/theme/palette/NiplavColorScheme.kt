package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFF0E4296)
private val ContainerColor = Color(0xFF21D3EF)
private val InterestColor = Color(0xFFF675D8)
private val BackgroundColor = Color(0xFF0261E3)
private val SelectedColor = Color(0xFFFEDb46)
private val TextColor = Color(0xFFFAFcF9)

val NiplavColorPalette = lightColorScheme(
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

val NiplavColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)