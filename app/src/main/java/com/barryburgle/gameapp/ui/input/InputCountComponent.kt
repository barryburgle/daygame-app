package com.barryburgle.gameapp.ui.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun InputCountComponent(
    inputTitle: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onEvent: (GenericEvent) -> Unit,
    countStart: Int? = 0,
    increment: Int? = 1,
    zeroValue: String? = "",
    saveEvent: (input: String) -> GenericEvent,
    @DrawableRes icon: Int? = 0,
) {
    var count by remember {
        mutableStateOf(if (countStart == null) 0 else countStart)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconShadowButton(
            onClick = {
                count--
                onEvent(saveEvent(count.toString()))
            }, imageVector = Icons.Default.Remove, contentDescription = "Less"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!zeroValue!!.isEmpty()) {
                InputCounter(
                    count = count,
                    style = style,
                    modifier = modifier,
                    zeroValue = zeroValue,
                    stringZero = true
                )
            } else {
                InputCounter(count = count, style = style, modifier = modifier)
            }
            if (icon != null && icon != 0) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "contacts",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(30.dp)
                )
            }
        }
        LittleBodyText(inputTitle)
        Spacer(modifier = Modifier.height(4.dp))
        IconShadowButton(
            onClick = {
                count += increment!!
                onEvent(saveEvent(count.toString()))
            }, imageVector = Icons.Default.Add, contentDescription = "More"
        )
    }
}