package com.barryburgle.gameapp.ui.utilities;

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.ImageShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun ToggleIcon(
    description: String,
    flag: Boolean,
    @DrawableRes icon: Int,
    dotCondition: Boolean? = false,
    onCheckedChange: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
        if (!flag) {
            iconColor = MaterialTheme.colorScheme.primary
        }
        Box(modifier = Modifier.size(50.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        clip = false
                    )
            ) {
                ImageShadowButton(
                    onClick = { },
                    icon = icon,
                    contentDescription = description,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    iconColor = iconColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.75f),
                    boxModifier = Modifier.fillMaxSize(),
                    dotCondition = dotCondition
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple()
                        ) {
                            onCheckedChange()
                        }
                )
            }
            if (flag) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Checked",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        LittleBodyText(description.replaceFirstChar { it.uppercase() })
    }
}