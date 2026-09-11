package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val VerstSemanticPalette = SemanticPalette(
    canvas = Color(0xFFF4314A),
    ink = Color(0xFF111013),
    card = Color(0xFFBC2830),
    overlay = Color(0xFFFDD296),
    tappable = Color(0xFFFDD296),
    onTappable = Color(0xFF111013),
    selectedFill = Color(0xFFF9BF26),
    selectedInk = Color(0xFF111013),
    control = Color(0xFF81151B),
    onControl = Color(0xFFE7E7EE),
    shimmer = Color(0xFFE32884),
    activeIcon = Color(0xFFF4371F)
)

val VerstColorPalette = VerstSemanticPalette.toColorScheme(isDark = false)

val VerstColorPaletteHint = ColorPaletteHint(
    VerstSemanticPalette.card,
    VerstSemanticPalette.selectedFill,
    VerstSemanticPalette.canvas
)