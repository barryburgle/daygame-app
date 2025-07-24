package com.barryburgle.gameapp.ui.utilities.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun CountSetting(
    text: String,
    count: Int,
    description: String? = null,
    onEvent: (GenericEvent) -> Unit,
    saveEvent: (input: String) -> GenericEvent
) {
    var settingCount = count
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.65f)
        ) {
            SmallTitleText(text)
            if (description != null) {
                LittleBodyText(description)
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconShadowButton(
                    onClick = {
                        settingCount--
                        onEvent(saveEvent(settingCount.toString()))
                    },
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Less"
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconShadowButton(
                    onClick = {
                        settingCount++
                        onEvent(saveEvent(settingCount.toString()))
                    },
                    imageVector = Icons.Default.Add,
                    contentDescription = "More"
                )
            }
        }
    }
}