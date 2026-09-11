package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val BroodySemanticPalette = SemanticPalette(
    canvas = Color(0xFF571A34),
    ink = Color(0xFFF7FFFE),
    card = Color(0xFF07345F),
    overlay = Color(0xFF126382),
    tappable = Color(0xFF126382),
    onTappable = Color(0xFFF7FFFE),
    selectedFill = Color(0xFF0B3D62),
    selectedInk = Color(0xFFD148A4),
    control = Color(0xFFD148A4),
    onControl = Color(0xFF0B3D62),
    shimmer = Color(0xFF571A34),
    activeIcon = Color(0xFFF7FFFE)
)

val BroodyColorPalette = BroodySemanticPalette.toColorScheme(isDark = true)

val BroodyColorPaletteHint = ColorPaletteHint(
    BroodySemanticPalette.card,
    BroodySemanticPalette.selectedFill,
    BroodySemanticPalette.canvas
)