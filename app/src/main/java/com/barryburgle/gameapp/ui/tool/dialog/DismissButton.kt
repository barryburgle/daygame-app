package com.barryburgle.gameapp.ui.tool.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton

@Composable
fun DismissButton(
    onClick: () -> Unit
) {
    IconShadowButton(
        onClick = onClick,
        imageVector = Icons.Default.Add,
        contentDescription = "Cancel",
        color = MaterialTheme.colorScheme.primaryContainer,
        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        boxModifier = Modifier
            .rotate(-45f),
        modifier = Modifier.scale(1.35f)
    )
}