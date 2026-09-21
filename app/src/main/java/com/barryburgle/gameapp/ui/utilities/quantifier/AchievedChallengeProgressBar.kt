package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.ui.utilities.animation.HorizontalProgressBarBrush
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import kotlinx.coroutines.delay

@Composable
fun AchievedChallengeProgressBar(achievedChallenge: AchievedChallenge) {
    val timePassingRatio = (achievedChallenge.getTimePassingPerc() * 100).toInt()
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
        BouncyProgressBar(
            fraction = completionRatio.toFloat() / 100,
            delayMs = 200L
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
    var timePassingDesc = "You already used ${timePassingRatio}% of your time"
    if (timePassingRatio > 100) {
        timePassingDesc = "Challenge time is over"
    }
    LittleBodyText(timePassingDesc)
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(5.dp)
            ), horizontalArrangement = Arrangement.Start
    ) {
        BouncyProgressBar(
            fraction = timePassingRatio.toFloat() / 100,
            delayMs = 300L
        )
    }
}

@Composable
private fun BouncyProgressBar(
    fraction: Float,
    delayMs: Long
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delayMs)
        isVisible = true
    }

    val transition = updateTransition(targetState = isVisible, label = "ProgressBarTransition")

    val scaleX by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "scaleX"
    ) { visible ->
        if (visible) 1f else 0f
    }

    val safeFraction = fraction.coerceIn(0f, 1f)
    if (safeFraction > 0f) {
        Row(
            modifier = Modifier
                .fillMaxWidth(safeFraction)
                .height(10.dp)
                .graphicsLayer {
                    this.scaleX = scaleX
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .background(
                    brush = HorizontalProgressBarBrush(MaterialTheme.colorScheme.onTertiary),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {}
    }
}