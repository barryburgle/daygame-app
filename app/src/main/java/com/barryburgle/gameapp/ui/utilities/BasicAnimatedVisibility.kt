package com.barryburgle.gameapp.ui.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable

@Composable
fun BasicAnimatedVisibility(visibilityFlag: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visibilityFlag, enter = fadeIn(
            animationSpec = tween(durationMillis = 150)
        ) + expandIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow
            )
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = 150)
        ) + shrinkOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow
            )
        )
    ) {
        content()
    }
}