package com.barryburgle.gameapp.ui.tool.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun TrackColor(flag: Boolean): Color {
    val trackColor by animateColorAsState(
        targetValue = if (flag) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 500)
    )
    return trackColor
}