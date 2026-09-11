package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val StealthSemanticPalette = SemanticPalette(
    canvas = Color(0xFF748EA5),
    ink = Color(0xFFFFFEFE),
    card = Color(0xFF002241),
    overlay = Color(0xFF00374D),
    tappable = Color(0xFF00374D),
    onTappable = Color(0xFFFFFEFE),
    selectedFill = Color(0xFF005C80),
    selectedInk = Color(0xFF011323),
    control = Color(0xFF002241),
    onControl = Color(0xFFFFFEFE),
    shimmer = Color(0xFF748EA5),
    activeIcon = Color(0xFFFFFEFE)
)

val StealthColorPalette = StealthSemanticPalette.toColorScheme(isDark = true)

val StealthColorPaletteHint = ColorPaletteHint(
    StealthSemanticPalette.card,
    StealthSemanticPalette.selectedFill,
    StealthSemanticPalette.canvas
)