package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Grey05
import com.barryburgle.gameapp.ui.theme.Grey10
import com.barryburgle.gameapp.ui.theme.Grey20
import com.barryburgle.gameapp.ui.theme.Grey50
import com.barryburgle.gameapp.ui.theme.Grey75

val DarkSemanticPalette = SemanticPalette(
    canvas = Grey10,
    ink = Grey75,
    card = Grey05,
    overlay = Grey10,
    tappable = Grey20,
    onTappable = Color.White,
    selectedFill = Grey05,
    selectedInk = Grey75,
    control = Grey50,
    onControl = Grey20,
    shimmer = Grey50,
    activeIcon = Grey50
)

val DarkColorPalette = DarkSemanticPalette.toColorScheme(isDark = true)

val DarkColorPaletteHint = ColorPaletteHint(
    DarkSemanticPalette.canvas,
    DarkSemanticPalette.tappable,
    DarkSemanticPalette.control
)