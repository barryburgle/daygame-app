package com.barryburgle.gameapp.ui.utilities.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

@Composable
fun ProgressBarBrush(color: Color): Brush {
    val transition = rememberInfiniteTransition(label = "timelineShimmer")
    val xOffset by transition.animateFloat(
        initialValue = 0f, targetValue = 4000f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "xOffset"
    )
    val shimmerTimeColors = listOf(
        color.copy(alpha = 0.9f),
        color.copy(alpha = 0.05f),
        color.copy(alpha = 0.9f),
    )
    return Brush.linearGradient(
        colors = shimmerTimeColors,
        start = Offset(xOffset - 1000f, 0f),
        end = Offset(xOffset, 0f),
        tileMode = TileMode.Clamp
    )
}