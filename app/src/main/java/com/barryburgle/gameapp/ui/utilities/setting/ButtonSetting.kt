package com.barryburgle.gameapp.ui.utilities.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.ShadowButton
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun ButtonSetting(
    text: String,
    imageVector: ImageVector,
    contentDescription: String?, onClick: () -> Unit
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
            ShadowButton(
                imageVector = imageVector,
                contentDescription = contentDescription,
                onClick = onClick
            )
        }
    }
}