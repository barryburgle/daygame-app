package com.barryburgle.gameapp.ui.utilities.text.body

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle

@Composable
fun LittleBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    italic: Boolean = false
) {
    if (italic) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = modifier,
            fontStyle = FontStyle.Italic
        )
    } else {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = modifier
        )
    }
}
