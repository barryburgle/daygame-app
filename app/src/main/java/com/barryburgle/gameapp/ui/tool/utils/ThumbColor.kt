package com.barryburgle.gameapp.ui.tool.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun ThumbColor(flag: Boolean): Color {
    val thumbColor by animateColorAsState(
        targetValue = if (flag) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 500)
    )
    return thumbColor
}