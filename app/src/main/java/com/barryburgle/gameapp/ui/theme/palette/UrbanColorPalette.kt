package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val UrbanSemanticPalette = SemanticPalette(
    canvas = Color(0xFF224954),
    ink = Color(0xFFFFF8F5),
    card = Color(0xFF112726),
    overlay = Color(0xFFF0972E),
    tappable = Color(0xFFF0972E),
    onTappable = Color(0xFFFFF8F5),
    selectedFill = Color(0xFFD46f2E),
    selectedInk = Color(0xFF020303),
    control = Color(0xFF112726),
    onControl = Color(0xFFFFF8F5),
    shimmer = Color(0xFF6393A0),
    activeIcon = Color(0xFFFFF8F5)
)

val UrbanColorPalette = UrbanSemanticPalette.toColorScheme(isDark = false)

val UrbanColorPaletteHint = ColorPaletteHint(
    UrbanSemanticPalette.card,
    UrbanSemanticPalette.selectedFill,
    UrbanSemanticPalette.canvas
)