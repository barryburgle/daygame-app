package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val JuliusSemanticPalette = SemanticPalette(
    canvas = Color(0xFF221D13),
    ink = Color(0xFFCCBF90),
    card = Color(0xFF856244),
    overlay = Color(0xFF887452),
    tappable = Color(0xFF887452),
    onTappable = Color(0xFFCCBF90),
    selectedFill = Color(0xFF403321),
    selectedInk = Color(0xFFEFE7C2),
    control = Color(0xFF856244),
    onControl = Color(0xFF221D13),
    shimmer = Color(0xFF221D13),
    activeIcon = Color(0xFFCCBF90)
)

val JuliusColorPalette = JuliusSemanticPalette.toColorScheme(isDark = true)

val JuliusColorPaletteHint = ColorPaletteHint(
    JuliusSemanticPalette.card,
    JuliusSemanticPalette.selectedFill,
    JuliusSemanticPalette.canvas
)