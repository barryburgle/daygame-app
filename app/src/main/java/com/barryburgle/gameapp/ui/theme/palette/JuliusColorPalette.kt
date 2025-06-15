package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val InterestColor = Color(0xFF403321)
private val ContainerColor = Color(0xFF856244)
private val SelectedColor = Color(0xFFEFE7C2)
private val TappableColor = Color(0xFF887452)
private val BackgroundColor = Color(0xFF221D13)
private val TextColor = Color(0xFFCCBF90)

val JuliusColorPalette = lightColorScheme(
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

val JuliusColorScheme = ColorPaletteHint(InterestColor, TextColor, BackgroundColor)