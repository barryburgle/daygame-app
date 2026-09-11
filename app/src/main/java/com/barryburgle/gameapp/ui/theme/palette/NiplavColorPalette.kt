package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val NiplavSemanticPalette = SemanticPalette(
    canvas = Color(0xFF0261E3),
    ink = Color(0xFFFAFcF9),
    card = Color(0xFF21D3EF),
    overlay = Color(0xFF0E4296),
    tappable = Color(0xFF0E4296),
    onTappable = Color(0xFFFAFcF9),
    selectedFill = Color(0xFFF675D8),
    selectedInk = Color(0xFFFEDb46),
    control = Color(0xFF21D3EF),
    onControl = Color(0xFFFAFcF9),
    shimmer = Color(0xFF0261E3),
    activeIcon = Color(0xFFFAFcF9)
)

val NiplavColorPalette = NiplavSemanticPalette.toColorScheme(isDark = false)

val NiplavColorPaletteHint = ColorPaletteHint(
    NiplavSemanticPalette.card,
    NiplavSemanticPalette.selectedFill,
    NiplavSemanticPalette.canvas
)