package com.barryburgle.gameapp.ui.utilities.text.body

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LittleBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    italic: Boolean = false,
    onLongClick: (() -> Unit)? = null
) {
    val clickModifier = if (onLongClick != null) {
        Modifier.combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { },
            onLongClick = onLongClick
        )
    } else {
        Modifier
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        modifier = modifier.then(clickModifier)
    )
}