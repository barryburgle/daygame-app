package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor =Color(0xFFBB3329)
private val ContainerColor = Color(0xFFB00205)
private val InterestColor = Color(0xFF595959)
private val BackgroundColor = Color(0xFFD9D9D9)
private val SelectedColor = Color(0xFF020302)
private val TextColor = Color(0xFFFFFEFE)

val HustleColorPalette = lightColorScheme(
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

val HustleColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor)