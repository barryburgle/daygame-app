package com.barryburgle.gameapp.ui.utilities.text.title

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MediumTitleText(
    text: String,
    black: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    if (black) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Black
        )
    } else {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}