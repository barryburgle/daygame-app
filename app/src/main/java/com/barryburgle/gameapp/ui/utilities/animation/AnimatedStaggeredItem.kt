package com.barryburgle.gameapp.ui.utilities.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun AnimatedStaggeredItem(
    index: Int,
    modifier: Modifier = Modifier,
    delayMs: Long = 80L,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * delayMs)
        isVisible = true
    }

    val transition = updateTransition(targetState = isVisible, label = "ItemTransition_$index")

    val scale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "scale"
    ) { visible ->
        if (visible) 1f else 0f
    }

    Box(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
            transformOrigin = TransformOrigin(0f, 0.5f)
        }
    ) {
        content()
    }
}