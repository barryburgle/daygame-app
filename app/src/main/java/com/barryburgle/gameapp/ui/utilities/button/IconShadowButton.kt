package com.barryburgle.gameapp.ui.utilities.button

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconShadowButton(
    onClick: () -> Unit,
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String?,
    title: String? = null,
    color: Color? = null,
    iconColor: Color? = null
) {
    var iconTint = MaterialTheme.colorScheme.inversePrimary
    if (iconColor != null) {
        iconTint = iconColor
    }
    GenericShadowButton(
        onClick = onClick,
        boxModifier = boxModifier,
        modifier = modifier,
        title = title,
        color = color,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier
                .height(20.dp)
                .scale(1.2f)
        )
    }
}