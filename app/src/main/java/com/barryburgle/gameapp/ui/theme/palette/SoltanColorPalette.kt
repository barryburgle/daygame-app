package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val SoltanSemanticPalette = SemanticPalette(
    canvas = Color(0xFF05147B),
    ink = Color(0xFFE8CD18),
    card = Color(0xFF030C4A),
    overlay = Color(0xFF05147B),
    tappable = Color(0xFF071DB0),
    onTappable = Color(0xFF7F8000),
    selectedFill = Color(0xFF030C4A),
    selectedInk = Color(0xFFFFFF01),
    control = Color(0xFF7F8000),
    onControl = Color(0xFF071DB0),
    shimmer = Color(0xFFA1901E),
    activeIcon = Color(0xFFCBB942)
)

val SoltanColorPalette = SoltanSemanticPalette.toColorScheme(isDark = true)

val SoltanColorPaletteHint = ColorPaletteHint(
    SoltanSemanticPalette.canvas,
    SoltanSemanticPalette.tappable,
    SoltanSemanticPalette.control
)