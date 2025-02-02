package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.input.InputCounter

@Composable
fun ToolCountComponent(
    inputTitle: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onEvent: (ToolEvent) -> Unit,
    saveEvent: (input: String) -> ToolEvent,
    initialCount: Int
) {
    var count by remember {
        mutableStateOf(initialCount)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = inputTitle, textAlign = TextAlign.Center)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                --count
                if (count < 1) {
                    count = 1
                }
                onEvent(saveEvent((count).toString()))
            }
        ) {
            Text(text = "-")
        }
        InputCounter(count = count, style = style, modifier = modifier)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                ++count
                if (count > 10) {
                    count = 10
                }
                onEvent(saveEvent((count).toString()))
            }
        ) {
            Text(text = "+")
        }
    }
}