package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.ui.graphics.Color

val BourbonSemanticPalette = SemanticPalette(
    canvas = Color(0xFF300900),
    ink = Color(0xFFC7B6A9),
    card = Color(0xFF4E0502),
    overlay = Color(0xFF4E0502),
    tappable = Color(0xFF4E0502),
    onTappable = Color(0xFFC7B6A9),
    selectedFill = Color(0xFF911200),
    selectedInk = Color(0xFFD67106),
    control = Color(0xFF8A6d5B),
    onControl = Color(0xFF300900),
    shimmer = Color(0xFF8A6d5B),
    activeIcon = Color(0xFFC7B6A9)
)

val BourbonColorPalette = BourbonSemanticPalette.toColorScheme(isDark = true)

val BourbonColorPaletteHint = ColorPaletteHint(
    BourbonSemanticPalette.card,
    BourbonSemanticPalette.selectedFill,
    BourbonSemanticPalette.canvas
)