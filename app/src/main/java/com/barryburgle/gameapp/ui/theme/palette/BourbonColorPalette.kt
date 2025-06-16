package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFF8A6d5B)
private val ContainerColor = Color(0xFF4E0502)
private val InterestColor = Color(0xFF911200)
private val BackgroundColor = Color(0xFF300900)
private val SelectedColor = Color(0xFFD67106)
private val TextColor = Color(0xFFC7B6A9)

val BourbonColorPalette = lightColorScheme(
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

val BourbonColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)