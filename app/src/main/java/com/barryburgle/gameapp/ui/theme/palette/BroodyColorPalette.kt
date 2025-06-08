package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFF126382)
private val ContainerColor = Color(0xFF07345F)
private val InterestColor = Color(0xFF0B3D62)
private val BackgroundColor = Color(0xFF571A34)
private val SelectedColor = Color(0xFFD148A4)
private val TextColor = Color(0xFFF7FFFE)

val BroodyColorPalette = darkColorScheme(
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

val BroodyColorPaletteHint = ColorPaletteHint(ContainerColor, BackgroundColor, TappableColor)