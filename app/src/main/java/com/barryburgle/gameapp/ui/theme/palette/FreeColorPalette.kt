package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFCDC0B2)
private val ContainerColor = Color(0xFFA9927A)
private val InterestColor = Color(0xFFB9A693)
private val BackgroundColor = Color(0xFF6C5A46)
private val SelectedColor = Color(0xFFE7E4E2)
private val TextColor = Color(0xFF2B251C)

val FreeColorPalette = darkColorScheme(
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

val FreeColorPaletteHint = ColorPaletteHint(ContainerColor, BackgroundColor, TappableColor)