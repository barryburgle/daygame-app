package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val MasterySemanticPalette = SemanticPalette(
    canvas = Color.White,
    ink = Color.Black,
    card = Color(0xFFFDFCE2),
    overlay = Color(0xFFB1825E),
    tappable = Color(0xFFB1825E),
    onTappable = Color.Black,
    selectedFill = Color(0xFFD0B385),
    selectedInk = Color(0xFF2F2014),
    control = Color(0xFF6C4E37),
    onControl = Color(0xFFFDFCE2),
    shimmer = Color(0xFF2F2014),
    activeIcon = Color(0xFFF2CF94)
)

val MasteryColorPalette = MasterySemanticPalette.toColorScheme(isDark = false)

val MasteryColorPaletteHint = ColorPaletteHint(
    MasterySemanticPalette.overlay,
    MasterySemanticPalette.selectedFill,
    MasterySemanticPalette.card
)