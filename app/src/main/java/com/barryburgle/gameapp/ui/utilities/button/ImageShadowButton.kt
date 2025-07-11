package com.barryburgle.gameapp.ui.utilities.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ImageShadowButton(
    onClick: () -> Unit,
    boxModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String?,
    title: String? = null,
    color: Color,
    iconColor: Color
) {
    GenericShadowButton(
        onClick = onClick,
        boxModifier = boxModifier,
        modifier = modifier,
        title = title,
        color = color
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}