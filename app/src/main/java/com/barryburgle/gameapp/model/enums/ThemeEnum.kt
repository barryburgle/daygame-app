package com.barryburgle.gameapp.model.enums

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
    FREE("free"),
    PHOENIX("phoenix"),
    BOURBON("bourbon"),
    NORTH("north"),
    RECOVER("recovering"),
    VERST("verstappen"),
    URBAN("urban"),
    NIPLAV("niplav"),
    BREEZE("breeze");

    companion object {

        @Composable
        fun useProperActiveColor(type: String): Color {
            return when (type) {
                BOURBON.type -> MaterialTheme.colorScheme.primary
                BREEZE.type -> MaterialTheme.colorScheme.onPrimary
                BROODY.type -> MaterialTheme.colorScheme.onSurfaceVariant
                CROWN.type -> MaterialTheme.colorScheme.onPrimary
                DARK.type -> MaterialTheme.colorScheme.primary
                FREE.type -> MaterialTheme.colorScheme.onPrimary
                HUSTLE.type -> MaterialTheme.colorScheme.onSurfaceVariant
                INFINITE.type -> MaterialTheme.colorScheme.primaryContainer
                JULIUS.type -> MaterialTheme.colorScheme.primaryContainer
                LIGHT.type -> MaterialTheme.colorScheme.onTertiary
                MASTERY.type -> MaterialTheme.colorScheme.onPrimary
                NIPLAV.type -> MaterialTheme.colorScheme.primary
                NORTH.type -> MaterialTheme.colorScheme.surface
                PHOENIX.type -> MaterialTheme.colorScheme.primary
                RECOVER.type -> MaterialTheme.colorScheme.onPrimary
                SOLTAN.type -> MaterialTheme.colorScheme.primary
                STEALTH.type -> MaterialTheme.colorScheme.onSurfaceVariant
                URBAN.type -> MaterialTheme.colorScheme.onSurfaceVariant
                VERST.type -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onPrimary
            }
        }

        @Composable
        fun useProperInactiveColor(type: String): Color {
            return when (type) {
                BOURBON.type -> MaterialTheme.colorScheme.onPrimary
                BREEZE.type -> MaterialTheme.colorScheme.primary
                BROODY.type -> MaterialTheme.colorScheme.primary
                CROWN.type -> MaterialTheme.colorScheme.surface
                DARK.type -> MaterialTheme.colorScheme.onPrimary
                FREE.type -> MaterialTheme.colorScheme.primaryContainer
                HUSTLE.type -> MaterialTheme.colorScheme.primaryContainer
                INFINITE.type -> MaterialTheme.colorScheme.onPrimary
                JULIUS.type -> MaterialTheme.colorScheme.onPrimary
                LIGHT.type -> MaterialTheme.colorScheme.primaryContainer
                MASTERY.type -> MaterialTheme.colorScheme.primary
                NIPLAV.type -> MaterialTheme.colorScheme.surface
                NORTH.type -> MaterialTheme.colorScheme.onPrimary
                PHOENIX.type -> MaterialTheme.colorScheme.onPrimary
                RECOVER.type -> MaterialTheme.colorScheme.primary
                SOLTAN.type -> MaterialTheme.colorScheme.onPrimary
                STEALTH.type -> MaterialTheme.colorScheme.background
                URBAN.type -> MaterialTheme.colorScheme.primary
                VERST.type -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onPrimary
            }
        }

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
                PHOENIX.type -> PhoenixColorPalette
                BOURBON.type -> BourbonColorPalette
                NORTH.type -> NorthColorPalette
                RECOVER.type -> RecoverColorPalette
                VERST.type -> VerstColorPalette
                URBAN.type -> UrbanColorPalette
                NIPLAV.type -> NiplavColorPalette
                BREEZE.type -> BreezeColorPalette
                else -> LightColorPalette
            }
        }

        fun sortedValues(): List<ThemeEnum> {
            return ThemeEnum.values().sortedBy { it.type }
        }

        fun randomValue(): ThemeEnum {
            return ThemeEnum.values().random()
        }
    }

    fun getFirstHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.containerColor
            LIGHT.type -> LightColorPaletteHint.containerColor
            INFINITE.type -> InfiniteColorPaletteHint.containerColor
            MASTERY.type -> MasteryColorPaletteHint.containerColor
            HUSTLE.type -> HustleColorPaletteHint.containerColor
            STEALTH.type -> StealthColorPaletteHint.containerColor
            CROWN.type -> CrownColorPaletteHint.containerColor
            BROODY.type -> BroodyColorPaletteHint.containerColor
            JULIUS.type -> JuliusColorPaletteHint.containerColor
            SOLTAN.type -> SoltanColorPaletteHint.containerColor
            FREE.type -> FreeColorPaletteHint.containerColor
            PHOENIX.type -> PhoenixColorPaletteHint.containerColor
            BOURBON.type -> BourbonColorPaletteHint.containerColor
            NORTH.type -> NorthColorPaletteHint.containerColor
            RECOVER.type -> RecoverColorPaletteHint.containerColor
            VERST.type -> VerstColorPaletteHint.containerColor
            URBAN.type -> UrbanColorPaletteHint.containerColor
            NIPLAV.type -> NiplavColorPaletteHint.containerColor
            BREEZE.type -> BreezeColorPaletteHint.containerColor
            else -> LightColorPaletteHint.containerColor
        }
    }

    fun getSecondHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.interestColor
            LIGHT.type -> LightColorPaletteHint.interestColor
            INFINITE.type -> InfiniteColorPaletteHint.interestColor
            MASTERY.type -> MasteryColorPaletteHint.interestColor
            HUSTLE.type -> HustleColorPaletteHint.interestColor
            STEALTH.type -> StealthColorPaletteHint.interestColor
            CROWN.type -> CrownColorPaletteHint.interestColor
            BROODY.type -> BroodyColorPaletteHint.interestColor
            JULIUS.type -> JuliusColorPaletteHint.interestColor
            SOLTAN.type -> SoltanColorPaletteHint.interestColor
            FREE.type -> FreeColorPaletteHint.interestColor
            PHOENIX.type -> PhoenixColorPaletteHint.interestColor
            BOURBON.type -> BourbonColorPaletteHint.interestColor
            NORTH.type -> NorthColorPaletteHint.interestColor
            RECOVER.type -> RecoverColorPaletteHint.interestColor
            VERST.type -> VerstColorPaletteHint.interestColor
            URBAN.type -> UrbanColorPaletteHint.interestColor
            NIPLAV.type -> NiplavColorPaletteHint.interestColor
            BREEZE.type -> BreezeColorPaletteHint.interestColor
            else -> LightColorPaletteHint.interestColor
        }
    }

    fun getThirdHint(): Color {
        return when (type) {
            DARK.type -> DarkColorPaletteHint.backgroundColor
            LIGHT.type -> LightColorPaletteHint.backgroundColor
            INFINITE.type -> InfiniteColorPaletteHint.backgroundColor
            MASTERY.type -> MasteryColorPaletteHint.backgroundColor
            HUSTLE.type -> HustleColorPaletteHint.backgroundColor
            STEALTH.type -> StealthColorPaletteHint.backgroundColor
            CROWN.type -> CrownColorPaletteHint.backgroundColor
            BROODY.type -> BroodyColorPaletteHint.backgroundColor
            JULIUS.type -> JuliusColorPaletteHint.backgroundColor
            SOLTAN.type -> SoltanColorPaletteHint.backgroundColor
            FREE.type -> FreeColorPaletteHint.backgroundColor
            PHOENIX.type -> PhoenixColorPaletteHint.backgroundColor
            BOURBON.type -> BourbonColorPaletteHint.backgroundColor
            NORTH.type -> NorthColorPaletteHint.backgroundColor
            RECOVER.type -> RecoverColorPaletteHint.backgroundColor
            VERST.type -> VerstColorPaletteHint.backgroundColor
            URBAN.type -> UrbanColorPaletteHint.backgroundColor
            NIPLAV.type -> NiplavColorPaletteHint.backgroundColor
            BREEZE.type -> BreezeColorPaletteHint.backgroundColor
            else -> LightColorPaletteHint.backgroundColor
        }
    }
}