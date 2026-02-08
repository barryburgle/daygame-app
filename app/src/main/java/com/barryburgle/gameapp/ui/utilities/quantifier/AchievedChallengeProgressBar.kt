package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun AchievedChallengeProgressBar(achievedChallenge: AchievedChallenge) {
    val completionRatio = (achievedChallenge.getCompletionPerc() * 100).toInt()
    var completionDesc = "Time to get on with some work"
    if (completionRatio > 0) {
        if (completionRatio < 100) {
            completionDesc = "Keep going, you are ${completionRatio}% there!"
        } else if (completionRatio == 100) {
            completionDesc = "Well done, you completed this challenge!"
        } else {
            completionDesc = "Fantastic! Results exceeded expectations by ${completionRatio - 100}%"
        }
    }
    LittleBodyText(completionDesc)
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(5.dp)
            ), horizontalArrangement = Arrangement.Start
    ) {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val xOffset by transition.animateFloat(
            initialValue = 0f, targetValue = 4000f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "xOffset"
        )
        val shimmerColors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
        )
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(xOffset - 1000f, 0f),
            end = Offset(xOffset, 0f),
            tileMode = TileMode.Clamp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(completionRatio.toFloat() / 100)
                .height(10.dp)
                .background(
                    brush = brush, shape = RoundedCornerShape(5.dp)
                )
        ) {}
    }
}