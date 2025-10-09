package com.barryburgle.gameapp.ui.utilities.selection

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButtonState

@Composable
fun SelectionRow(
    currentSort: FieldEnum,
    sortType: FieldEnum,
    onEvent: (GenericEvent) -> Unit,
    genericEvent: GenericEvent
) {
    // TODO: create a tripe-state selection process where either don't sort, sort ascending or sort descending by field
    // TODO: put on the left of every button (animating similarly to MultiChoiceButton) text either [no-icon = no selection, up-arrow = sort ascending, down-arrow = sort descending]
    val textBackgroundColor =
        if (currentSort == sortType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val textButtonColor =
        if (currentSort == sortType) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
    var buttonState by remember { mutableStateOf(IconShadowButtonState.IDLE) }
    val scale by animateFloatAsState(
        targetValue = if (buttonState == IconShadowButtonState.PRESSED) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 400f
        ),
        label = "ButtonScaleAnimation"
    )
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        buttonState = IconShadowButtonState.PRESSED
                        waitForUpOrCancellation()
                        buttonState = IconShadowButtonState.IDLE
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            )
            .background(
                color = textBackgroundColor,
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier.width(100.dp),
            onClick = {
                onEvent(genericEvent)
            }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AnnotatedString(sortType.getField()),
                    color = textButtonColor,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}