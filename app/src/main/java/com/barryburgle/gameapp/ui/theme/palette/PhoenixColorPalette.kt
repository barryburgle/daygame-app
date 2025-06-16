package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFB2280F)
private val ContainerColor = Color(0xFF73120A)
private val InterestColor = Color(0xFF2B0804)
private val BackgroundColor = Color(0xFFF28F22)
private val SelectedColor = Color(0xFFD74916)
private val TextColor = Color(0xFFFEF4CE)

val PhoenixColorPalette = lightColorScheme(
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

val PhoenixColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)