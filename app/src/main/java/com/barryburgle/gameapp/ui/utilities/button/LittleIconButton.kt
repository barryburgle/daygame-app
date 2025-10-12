package com.barryburgle.gameapp.ui.utilities.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun LittleIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector
) {
    IconButton(
        modifier = Modifier,
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
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .height(10.dp)
                    .scale(2f)
            )
        }
    }
}