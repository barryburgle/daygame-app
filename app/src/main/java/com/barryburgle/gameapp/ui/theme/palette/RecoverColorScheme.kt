package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

private val TappableColor = Color(0xFFCEF1C7)
private val ContainerColor = Color(0xFF9DE490)
private val InterestColor = Color(0xFFADE8A2)
private val BackgroundColor = Color(0xFFEFFAEC)
private val SelectedColor = Color(0xFFBF00FF)
private val TextColor = Color(0xFF020105)

val RecoverColorPalette = lightColorScheme(
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

val RecoverColorPaletteHint = ColorPaletteHint(ContainerColor, InterestColor, BackgroundColor)