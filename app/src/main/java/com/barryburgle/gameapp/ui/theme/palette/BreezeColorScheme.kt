package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFBAEBFF)
private val ContainerColor = Color(0xFF01B5FE)
private val InterestColor = Color(0xFF8BFFFF)
private val BackgroundColor = Color(0xFFFEFFFF)
private val SelectedColor = Color(0xFF00D1D1)
private val TextColor = Color(0xFF1D394D)


val BreezeColorPalette = lightColorScheme(
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

val BreezeColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)