package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val PhoenixSemanticPalette = SemanticPalette(
    canvas = Color(0xFFF28F22),
    ink = Color(0xFFFEF4CE),
    card = Color(0xFF73120A),
    overlay = Color(0xFFB2280F),
    tappable = Color(0xFFB2280F),
    onTappable = Color(0xFFFEF4CE),
    selectedFill = Color(0xFF2B0804),
    selectedInk = Color(0xFFD74916),
    control = Color(0xFFC03D25),
    onControl = Color(0xFFFEF4CE),
    shimmer = Color(0xFFF28F22),
    activeIcon = Color(0xFFFEF4CE)
)

val PhoenixColorPalette = PhoenixSemanticPalette.toColorScheme(isDark = false)

val PhoenixColorPaletteHint = ColorPaletteHint(
    PhoenixSemanticPalette.card,
    PhoenixSemanticPalette.selectedFill,
    PhoenixSemanticPalette.canvas
)