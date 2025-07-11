package com.barryburgle.gameapp.ui.utilities;

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.ImageShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun ToggleIcon(
    description: String,
    flag: Boolean,
    smallerIcon: Boolean,
    @DrawableRes icon: Int,
    onCheckedChange: () -> Unit
) {
    // TODO: instead of having descriptions with or without "Not", display a little badge (Icons.Filled.CheckCircle) with a tick on the bottom right of the icon [v1.7.3]
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var iconColor: Color = MaterialTheme.colorScheme.secondaryContainer
        if (!flag) {
            iconColor = MaterialTheme.colorScheme.inversePrimary
        }
        var iconModifier: Modifier = Modifier
            .fillMaxSize()
            .padding(7.dp)
        if (smallerIcon) {
            iconModifier = Modifier
                .fillMaxSize(0.88f)
                .padding(7.dp)
        }
        ImageShadowButton(
            onClick = {
                onCheckedChange()
            },
            icon = icon,
            contentDescription = description,
            color = MaterialTheme.colorScheme.background,
            iconColor = iconColor,
            modifier = iconModifier,
            boxModifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        LittleBodyText(description.replaceFirstChar { it.uppercase() })
    }
}