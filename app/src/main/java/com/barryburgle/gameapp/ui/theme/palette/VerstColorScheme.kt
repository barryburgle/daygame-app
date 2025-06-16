package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFFDD296)
private val ContainerColor = Color(0xFFBC2830)
private val InterestColor = Color(0xFFF9BF26)
private val BackgroundColor = Color(0xFFE0465A)
private val SelectedColor = Color(0xFFE7E7EE)
private val TextColor = Color(0xFF111013)

val VerstColorPalette = lightColorScheme(
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

val VerstColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)