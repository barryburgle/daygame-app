package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Grey30
import com.barryburgle.gameapp.ui.theme.Grey50
import com.barryburgle.gameapp.ui.theme.Grey65
import com.barryburgle.gameapp.ui.theme.Grey70
import com.barryburgle.gameapp.ui.theme.Grey85
import com.barryburgle.gameapp.ui.theme.Grey95

val LightSemanticPalette = SemanticPalette(
    canvas = Color.White,
    ink = Color.Black,
    card = Grey95,
    overlay = Grey65,
    tappable = Grey65,
    onTappable = Color.Black,
    selectedFill = Grey85,
    selectedInk = Color.Black,
    control = Grey50,
    onControl = Grey95,
    shimmer = Grey30,
    activeIcon = Grey70
)

val LightColorPalette = LightSemanticPalette.toColorScheme(isDark = false)

val LightColorPaletteHint = ColorPaletteHint(
    LightSemanticPalette.activeIcon,
    LightSemanticPalette.canvas,
    LightSemanticPalette.shimmer
)