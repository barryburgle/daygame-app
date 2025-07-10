package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.TextStyle
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.ui.utilities.button.ShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun InputCountComponent(
    inputTitle: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onEvent: (GenericEvent) -> Unit,
    countStart: Int? = 0,
    saveEvent: (input: String) -> GenericEvent
) {
    var count by remember {
        mutableStateOf(if (countStart == null) 0 else countStart)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LittleBodyText(inputTitle)
        ShadowButton(
            onClick = {
                count--
                onEvent(saveEvent(count.toString()))
            },
            imageVector = Icons.Default.Remove,
            contentDescription = "Less"
        )
        InputCounter(count = count, style = style, modifier = modifier)
        ShadowButton(
            onClick = {
                count++
                onEvent(saveEvent(count.toString()))
            },
            imageVector = Icons.Default.Add,
            contentDescription = "More"
        )
    }
}