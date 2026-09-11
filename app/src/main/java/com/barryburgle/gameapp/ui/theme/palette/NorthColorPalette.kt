package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val NorthSemanticPalette = SemanticPalette(
    canvas = Color(0xFFD5BB87),
    ink = Color(0xFFF6E6BF),
    card = Color(0xFF221233),
    overlay = Color(0xFF655D52),
    tappable = Color(0xFF655D52),
    onTappable = Color(0xFFF6E6BF),
    selectedFill = Color(0xFF332B3A),
    selectedInk = Color(0xFFF33D07),
    control = Color(0xFF0E0814),
    onControl = Color(0xFFF6E6BF),
    shimmer = Color(0xFFD5BB87),
    activeIcon = Color(0xFFF6E6BF)
)

val NorthColorPalette = NorthSemanticPalette.toColorScheme(isDark = true)

val NorthColorPaletteHint = ColorPaletteHint(
    NorthSemanticPalette.card,
    NorthSemanticPalette.selectedFill,
    NorthSemanticPalette.canvas
)