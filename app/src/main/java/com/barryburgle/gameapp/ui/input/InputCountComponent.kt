package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent

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
        Text(text = inputTitle, textAlign = TextAlign.Center)
        IconButton(
            onClick = {
                count--
                onEvent(saveEvent(count.toString()))
            }, modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .size(40.dp)
        ) {
            Text(text = "-")
        }
        InputCounter(count = count, style = style, modifier = modifier)
        IconButton(
            onClick = {
                count++
                onEvent(saveEvent(count.toString()))
            }, modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .size(40.dp)
        ) {
            Text(text = "+")
        }
    }
}