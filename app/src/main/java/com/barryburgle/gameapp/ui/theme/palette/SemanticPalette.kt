package com.barryburgle.gameapp.ui.theme.palette

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.Red30

data class SemanticPalette(
    val canvas: Color,
    val ink: Color,
    val card: Color,
    val overlay: Color,
    val tappable: Color,
    val onTappable: Color,
    val selectedFill: Color,
    val selectedInk: Color,
    val control: Color,
    val onControl: Color,
    val shimmer: Color,
    val activeIcon: Color,
    val danger: Color = Red30
) {
    fun toColorScheme(isDark: Boolean = false): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                background = canvas,
                onPrimary = ink,
                onSurface = ink,
                outline = ink,
                secondary = ink,
                surface = card,
                surfaceVariant = overlay,
                primaryContainer = tappable,
                onPrimaryContainer = onTappable,
                primary = selectedFill,
                onSurfaceVariant = selectedInk,
                tertiary = control,
                inversePrimary = onControl,
                onTertiary = shimmer,
                secondaryContainer = activeIcon,
                onErrorContainer = danger
            )
        } else {
            lightColorScheme(
                background = canvas,
                onPrimary = ink,
                onSurface = ink,
                outline = ink,
                secondary = ink,
                surface = card,
                surfaceVariant = overlay,
                primaryContainer = tappable,
                onPrimaryContainer = onTappable,
                primary = selectedFill,
                onSurfaceVariant = selectedInk,
                tertiary = control,
                inversePrimary = onControl,
                onTertiary = shimmer,
                secondaryContainer = activeIcon,
                onErrorContainer = danger
            )
        }
    }
}