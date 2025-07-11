package com.barryburgle.gameapp.ui.tool.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton

@Composable
fun ConfirmButton(
    onClick: () -> Unit
) {
    IconShadowButton(
        onClick = onClick,
        imageVector = Icons.Default.Check,
        contentDescription = "Save",
        color = MaterialTheme.colorScheme.primaryContainer,
        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.scale(1.35f)
    )
}