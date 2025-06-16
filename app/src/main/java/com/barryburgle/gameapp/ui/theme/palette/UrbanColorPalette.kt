package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFF0972E)
private val ContainerColor = Color(0xFF112726)
private val InterestColor = Color(0xFFD46f2E)
private val BackgroundColor = Color(0xFF224954)
private val SelectedColor = Color(0xFF020303)
private val TextColor = Color(0xFFFFF8F5)

val UrbanColorPalette = lightColorScheme(
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

val UrbanColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)