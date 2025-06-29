package com.barryburgle.gameapp.ui.utilities;

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        IconButton(
            onClick = {
                onCheckedChange()
            }, modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .size(40.dp)
        ) {
            var color: Color = MaterialTheme.colorScheme.onPrimary
            if (!flag) {
                color = MaterialTheme.colorScheme.primary
            }
            var iconModifier: Modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
            if (smallerIcon) {
                iconModifier = Modifier
                    .fillMaxSize(0.88f)
                    .padding(7.dp)
            }
            Image(
                painter = painterResource(icon),
                contentDescription = description,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = iconModifier,
                colorFilter = ColorFilter.tint(color)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        LittleBodyText(description.replaceFirstChar { it.uppercase() })
    }
}