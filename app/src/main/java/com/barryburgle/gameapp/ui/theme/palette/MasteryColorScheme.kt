package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

// TODO: re-do theme with color structure similar to Hustle and rename colorscheme to color palette

private val FirstColor = Color(0xFFD0B385)
private val SecondColor = Color(0xFFB1825E)
private val ThirdColor = Color(0xFFFDFCE2)
private val FourthColor = Color.Black
private val FifthColor = Color(0xFFF2CF94)
private val ErrorColor = Red30
private val SixthColor = Color.White
private val SeventhColor = Color(0xFF6C4E37)
private val EightColor = Color(0xFF2F2014)

val MasteryColorScheme = lightColorScheme(
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

val MasteryColorPaletteHint = ColorPaletteHint(SecondColor, FirstColor)