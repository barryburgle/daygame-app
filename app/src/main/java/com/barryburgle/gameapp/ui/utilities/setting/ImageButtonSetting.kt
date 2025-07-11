package com.barryburgle.gameapp.ui.utilities.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.ImageShadowButton
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun ImageButtonSetting(
    text: String,
    @DrawableRes icon: Int,
    contentDescription: String?, onClick: () -> Unit,
    color: Color? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
        ) {
            SmallTitleText(text)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageShadowButton(
                icon = icon,
                contentDescription = contentDescription,
                onClick = onClick,
                color = color
            )
        }
    }
}