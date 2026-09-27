package com.barryburgle.gameapp.ui.input.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.input.liveSessionPulsingColor

@Composable
fun FloatingInputButton(
    icon: ImageVector? = null,
    description: String,
    animate: Boolean = false,
    animationColor: Color? = MaterialTheme.colorScheme.background,
    @DrawableRes drawableRes: Int? = null,
    onClick: () -> Unit
) {
    val tint =
        if (animate) liveSessionPulsingColor(animationColor) else MaterialTheme.colorScheme.inversePrimary

    Column(
        modifier = Modifier.height(80.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            containerColor = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        tint = tint
                    )
                }

                drawableRes != null -> {
                    Icon(
                        painter = painterResource(id = drawableRes),
                        contentDescription = description,
                        tint = tint,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}