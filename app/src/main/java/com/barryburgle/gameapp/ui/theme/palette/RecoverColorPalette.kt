package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val RecoverSemanticPalette = SemanticPalette(
    canvas = Color(0xFFEFFAEC),
    ink = Color(0xFF020105),
    card = Color(0xFF9DE490),
    overlay = Color(0xFFCEF1C7),
    tappable = Color(0xFFCEF1C7),
    onTappable = Color(0xFF020105),
    selectedFill = Color(0xFFADE8A2),
    selectedInk = Color(0xFFBF00FF),
    control = Color(0xFF9DE490),
    onControl = Color(0xFF020105),
    shimmer = Color(0xFFEFFAEC),
    activeIcon = Color(0xFF020105)
)

val RecoverColorPalette = RecoverSemanticPalette.toColorScheme(isDark = false)

val RecoverColorPaletteHint = ColorPaletteHint(
    RecoverSemanticPalette.card,
    RecoverSemanticPalette.selectedFill,
    RecoverSemanticPalette.canvas
)