package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val InfiniteSemanticPalette = SemanticPalette(
    canvas = Color(0xFF857154),
    ink = Color(0xFFC2BFAC),
    card = Color(0xFF2555c1),
    overlay = Color(0xFF4A504A),
    tappable = Color(0xFF4A504A),
    onTappable = Color(0xFFC2BFAC),
    selectedFill = Color(0xFF171E4F),
    selectedInk = Color(0xFF42B4C7),
    control = Color(0xFF2555c1),
    onControl = Color(0xFF857154),
    shimmer = Color(0xFF857154),
    activeIcon = Color(0xFFC2BFAC)
)

val InfiniteColorPalette = InfiniteSemanticPalette.toColorScheme(isDark = false)

val InfiniteColorPaletteHint = ColorPaletteHint(
    InfiniteSemanticPalette.card,
    InfiniteSemanticPalette.selectedFill,
    InfiniteSemanticPalette.canvas
)