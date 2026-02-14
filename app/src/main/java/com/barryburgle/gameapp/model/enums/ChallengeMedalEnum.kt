package com.barryburgle.gameapp.model.enums

import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.R

val Gold = Color(0xFFFFD700)
val Silver = Color(0xFFA9A9A9)
val Bronze = Color(0xFFCD7F32)
val Steel = Color(0xFF757575)

enum class ChallengeMedalEnum(
    private val description: String,
    private val icon: Int,
    private val color: Color
) {
    GOLD("Gold\nMedal", R.drawable.gold_medal, Gold),
    SILVER("Silver\nMedal", R.drawable.silver_medal, Silver),
    BRONZE("Bronze\nMedal", R.drawable.bronze_medal, Bronze),
    NONE("Completed", R.drawable.completed_medal, Steel),
    ONGOING("Ongoing", R.drawable.ongoing, Color.Transparent);


    fun getDescription(): String {
        return description
    }

    fun getIcon(): Int {
        return icon
    }

    fun getColor(): Color {
        return color
    }
}