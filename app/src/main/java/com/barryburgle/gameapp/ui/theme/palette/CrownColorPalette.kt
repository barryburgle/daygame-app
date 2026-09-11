package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val CrownSemanticPalette = SemanticPalette(
    canvas = Color(0xFFDCCCAC),
    ink = Color(0xFF13100C),
    card = Color(0xFF8F0A22),
    overlay = Color(0xFFC6B084),
    tappable = Color(0xFFC6B084),
    onTappable = Color(0xFF13100C),
    selectedFill = Color(0xFFFFFCFF),
    selectedInk = Color(0xFF9D8148),
    control = Color(0xFFCA213F),
    onControl = Color(0xFFFFFCFF),
    shimmer = Color(0xFFDCCCAC),
    activeIcon = Color(0xFF13100C)
)

val CrownColorPalette = CrownSemanticPalette.toColorScheme(isDark = false)

val CrownColorPaletteHint = ColorPaletteHint(
    CrownSemanticPalette.card,
    CrownSemanticPalette.selectedFill,
    CrownSemanticPalette.canvas
)