package com.barryburgle.gameapp.model.enums

import androidx.compose.ui.graphics.Color
import com.barryburgle.gameapp.R

enum class ChallengeMedalEnum(
    private val description: String,
    private val icon: Int,
    private val color: Color
) {
    GOLD("Gold\nMedal", R.drawable.gold_medal, Color(0xFFFFD700)),
    SILVER("Silver\nMedal", R.drawable.silver_medal, Color(0xFFA9A9A9)),
    BRONZE("Bronze\nMedal", R.drawable.bronze_medal, Color(0xFFCD7F32)),
    NONE("Completed", R.drawable.completed_medal, Color(0xFF757575)),
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