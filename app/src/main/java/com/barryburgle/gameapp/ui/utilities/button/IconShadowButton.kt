package com.barryburgle.gameapp.ui.utilities.button

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconShadowButton(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier.scale(1.2f),
    iconModifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    drawableIcon: Int = 0,
    secondaryImageVector: ImageVector? = null,
    secondaryDrawableIcon: Int = 0,
    contentDescription: String?,
    title: String? = null,
    color: Color? = null,
    glowing: Boolean? = false,
    iconColor: Color? = null
) {
    var iconTint = MaterialTheme.colorScheme.inversePrimary
    if (iconColor != null) {
        iconTint = iconColor
    }

    val unifiedBoxModifier = boxModifier
        .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
        .shadow(elevation = 4.dp, shape = CircleShape, clip = false)

    val unifiedModifier =
        modifier
            .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
            .clip(CircleShape)

    Box {
        GenericShadowButton(
            onClick = onClick,
            onLongClick = onLongClick ?: {},
            boxModifier = unifiedBoxModifier,
            modifier = unifiedModifier,
            title = title,
            color = color,
            glowing = glowing
        ) {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                        .height(20.dp)
                        .scale(1.2f)
                )
            } else if (drawableIcon != 0) {
                Icon(
                    painter = painterResource(drawableIcon),
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                        .height(20.dp)
                        .scale(1.2f)
                )
            }
        }
        if (secondaryImageVector != null) {
            Icon(
                imageVector = secondaryImageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f), CircleShape)
                    .scale(0.8f)
            )
        } else if (secondaryDrawableIcon != 0) {
            Icon(
                painter = painterResource(secondaryDrawableIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f), CircleShape)
                    .scale(0.8f)
            )
        }

        val haptic = LocalHapticFeedback.current
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .then(
                    if (onLongClick != null) {
                        Modifier.combinedClickable(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                            onClick()
                        }, onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onLongClick()
                        })
                    } else {
                        Modifier.clickable(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                            onClick()
                        })
                    }
                )
        )
    }
}