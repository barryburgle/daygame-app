package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val HustleSemanticPalette = SemanticPalette(
    canvas = Color(0xFFA7A7A7),
    ink = Color(0xFFFFFEFE),
    card = Color(0xFF922820),
    overlay = Color(0xFFD8574e),
    tappable = Color(0xFFD8574e),
    onTappable = Color(0xFFFFFEFE),
    selectedFill = Color(0xFF595959),
    selectedInk = Color(0xFF020302),
    control = Color(0xFF922820),
    onControl = Color(0xFFA7A7A7),
    shimmer = Color(0xFFA7A7A7),
    activeIcon = Color(0xFFFFFEFE)
)

val HustleColorPalette = HustleSemanticPalette.toColorScheme(isDark = false)

val HustleColorPaletteHint = ColorPaletteHint(
    HustleSemanticPalette.card,
    HustleSemanticPalette.selectedFill,
    HustleSemanticPalette.canvas
)