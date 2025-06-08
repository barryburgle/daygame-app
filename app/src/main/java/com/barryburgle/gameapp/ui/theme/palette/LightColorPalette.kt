package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Grey30
import com.barryburgle.gameapp.ui.theme.Grey50
import com.barryburgle.gameapp.ui.theme.Grey65
import com.barryburgle.gameapp.ui.theme.Grey70
import com.barryburgle.gameapp.ui.theme.Grey85
import com.barryburgle.gameapp.ui.theme.Grey95
import com.barryburgle.gameapp.ui.theme.Red30

private val FirstColor = Grey85
private val SecondColor = Grey65
private val ThirdColor = Grey95
private val FourthColor = Color.Black
private val FifthColor = Grey70
private val ErrorColor = Red30
private val SixthColor = Color.White
private val SeventhColor = Grey50
private val EightColor = Grey30

val LightColorPalette = lightColorScheme(
    primary = FirstColor,
    onPrimary = FourthColor,
    primaryContainer = SecondColor,
    onPrimaryContainer = FourthColor,
    inversePrimary = ThirdColor,
    secondary = FourthColor,
    secondaryContainer = FifthColor,
    onErrorContainer = ErrorColor,
    background = SixthColor,
    surface = ThirdColor,
    onSurface = FourthColor,
    surfaceVariant = SecondColor,
    onSurfaceVariant = FourthColor,
    outline = FourthColor,
    tertiary = SeventhColor,
    onTertiary = EightColor
)

val LightColorPaletteHint = ColorPaletteHint(Grey70, Color.White)