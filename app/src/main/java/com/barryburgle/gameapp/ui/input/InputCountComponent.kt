package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.barryburgle.gameapp.event.AbstractSessionEvent

@Composable
fun InputCountComponent(
    inputTitle: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onEvent: (AbstractSessionEvent) -> Unit,
    countStart: Int? = 0,
    saveEvent: (input: String) -> AbstractSessionEvent
) {
    var count by remember {
        mutableStateOf(if (countStart == null) 0 else countStart)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = inputTitle, textAlign = TextAlign.Center)
        Button(
            onClick = {
                count--
                onEvent(saveEvent(count.toString()))
            }
        ) {
            Text(text = "-")
        }
        InputCounter(count = count, style = style, modifier = modifier)
        Button(
            onClick = {
                count++
                onEvent(saveEvent(count.toString()))
            }
        ) {
            Text(text = "+")
        }
    }
}