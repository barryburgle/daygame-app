package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val FreeSemanticPalette = SemanticPalette(
    canvas = Color(0xFF6C5A46),
    ink = Color(0xFF2B251C),
    card = Color(0xFFA9927A),
    overlay = Color(0xFFCDC0B2),
    tappable = Color(0xFFCDC0B2),
    onTappable = Color(0xFF2B251C),
    selectedFill = Color(0xFF8F8174),
    selectedInk = Color(0xFFE7E4E2),
    control = Color(0xFFE7E4E2),
    onControl = Color(0xFF6C5A46),
    shimmer = Color(0xFF6C5A46),
    activeIcon = Color(0xFF2B251C)
)

val FreeColorPalette = FreeSemanticPalette.toColorScheme(isDark = true)

val FreeColorPaletteHint = ColorPaletteHint(
    FreeSemanticPalette.card,
    FreeSemanticPalette.selectedFill,
    FreeSemanticPalette.canvas
)