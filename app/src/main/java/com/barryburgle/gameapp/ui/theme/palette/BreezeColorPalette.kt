package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val BreezeSemanticPalette = SemanticPalette(
    canvas = Color(0xFFFEFFFF),
    ink = Color(0xFF1D394D),
    card = Color(0xFF01B5FE),
    overlay = Color(0xFFBAEBFF),
    tappable = Color(0xFFBAEBFF),
    onTappable = Color(0xFF1D394D),
    selectedFill = Color(0xFF8BFFFF),
    selectedInk = Color(0xFF00D1D1),
    control = Color(0xFF01B5FE),
    onControl = Color(0xFF8BFFFF),
    shimmer = Color(0xFFFEFFFF),
    activeIcon = Color(0xFF1D394D)
)

val BreezeColorPalette = BreezeSemanticPalette.toColorScheme(isDark = false)

val BreezeColorPaletteHint = ColorPaletteHint(
    BreezeSemanticPalette.card,
    BreezeSemanticPalette.selectedFill,
    BreezeSemanticPalette.canvas
)