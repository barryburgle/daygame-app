package com.barryburgle.gameapp.model.enums

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.ui.theme.palette.*

enum class ThemeEnum(val type: String) {
    DARK("dark"),
    LIGHT("light"),
    INFINITE("infinite"),
    MASTERY("mastery"),
    HUSTLE("hustle"),
    STEALTH("stealth"),
    CROWN("crown"),
    BROODY("broody"),
    JULIUS("julius"),
    SOLTAN("soltan"),
    FREE("free");

    companion object {
        fun getTheme(type: String): ColorScheme {
            return when (type) {
                DARK.type -> DarkColorPalette
                LIGHT.type -> LightColorPalette
                INFINITE.type -> InfiniteColorPalette
                MASTERY.type -> MasteryColorPalette
                HUSTLE.type -> HustleColorPalette
                STEALTH.type -> StealthColorPalette
                CROWN.type -> CrownColorPalette
                BROODY.type -> BroodyColorPalette
                JULIUS.type -> JuliusColorPalette
                SOLTAN.type -> SoltanColorPalette
                FREE.type -> FreeColorPalette
                else -> LightColorPalette
            }
        }

        fun sortedValues(): List<ThemeEnum> {
            return ThemeEnum.values().sortedBy { it.type }
        }
    }

    fun getFirstHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.firstColor
            LIGHT.type -> LightColorPaletteHint.firstColor
            INFINITE.type -> InfiniteColorPaletteHint.firstColor
            MASTERY.type -> MasteryColorPaletteHint.firstColor
            HUSTLE.type -> HustleColorPaletteHint.firstColor
            STEALTH.type -> StealthColorPaletteHint.firstColor
            CROWN.type -> CrownColorPaletteHint.firstColor
            BROODY.type -> BroodyColorPaletteHint.firstColor
            JULIUS.type -> JuliusColorPaletteHint.firstColor
            SOLTAN.type -> SoltanColorPaletteHint.firstColor
            FREE.type -> FreeColorPaletteHint.firstColor
            else -> LightColorPaletteHint.firstColor
        }
    }

    fun getSecondHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.secondColor
            LIGHT.type -> LightColorPaletteHint.secondColor
            INFINITE.type -> InfiniteColorPaletteHint.secondColor
            MASTERY.type -> MasteryColorPaletteHint.secondColor
            HUSTLE.type -> HustleColorPaletteHint.secondColor
            STEALTH.type -> StealthColorPaletteHint.secondColor
            CROWN.type -> CrownColorPaletteHint.secondColor
            BROODY.type -> BroodyColorPaletteHint.secondColor
            JULIUS.type -> JuliusColorPaletteHint.secondColor
            SOLTAN.type -> SoltanColorPaletteHint.secondColor
            FREE.type -> FreeColorPaletteHint.secondColor
            else -> LightColorPaletteHint.secondColor
        }
    }

    fun getThirdHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.thirdColor
            LIGHT.type -> LightColorPaletteHint.thirdColor
            INFINITE.type -> InfiniteColorPaletteHint.thirdColor
            MASTERY.type -> MasteryColorPaletteHint.thirdColor
            HUSTLE.type -> HustleColorPaletteHint.thirdColor
            STEALTH.type -> StealthColorPaletteHint.thirdColor
            CROWN.type -> CrownColorPaletteHint.thirdColor
            BROODY.type -> BroodyColorPaletteHint.thirdColor
            JULIUS.type -> JuliusColorPaletteHint.thirdColor
            SOLTAN.type -> SoltanColorPaletteHint.thirdColor
            FREE.type -> FreeColorPaletteHint.thirdColor
            else -> LightColorPaletteHint.thirdColor
        }
    }
}