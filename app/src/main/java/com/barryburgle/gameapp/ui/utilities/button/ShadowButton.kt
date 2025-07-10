package com.barryburgle.gameapp.ui.utilities.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

enum class ShadowButtonState { PRESSED, IDLE }

@Composable
fun ShadowButton(
    onClick: () -> Unit,
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String?,
    title: String? = null,
    color: Color? = null,
    iconColor: Color? = null
) {
    var buttonState by remember { mutableStateOf(ShadowButtonState.IDLE) }
    val scale by animateFloatAsState(
        targetValue = if (buttonState == ShadowButtonState.PRESSED) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 400f
        ),
        label = "ButtonScaleAnimation"
    )
    val haptic = LocalHapticFeedback.current
    var backgroundColor = MaterialTheme.colorScheme.tertiary
    if (color != null) {
        backgroundColor = color
    }
    var iconTint = MaterialTheme.colorScheme.inversePrimary
    if (iconColor != null) {
        iconTint = iconColor
    }
    Box(
        modifier = boxModifier
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        buttonState = ShadowButtonState.PRESSED
                        waitForUpOrCancellation()
                        buttonState = ShadowButtonState.IDLE
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                clip = false
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = modifier,
            onClick = {
                onClick()
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier
                        .height(20.dp)
                        .scale(1.2f)
                )
                if (title != null && !title.isBlank()) {
                    SmallTitleText(title)
                }
            }
        }
    }
}