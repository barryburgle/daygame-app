package com.barryburgle.gameapp.ui.input

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun InputCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    // TODO: doesn't handle the numbers above 9, fix it
    var oldCount by remember {
        mutableStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        var countString = count.toString()
        val oldCountString = oldCount.toString()
        if (count < 0) {
            Text(
                text = "0",
                style = style,
                softWrap = false,
                fontSize = 35.sp
            )
        } else {
            for (charIndex in countString.indices) {
                val oldChar = oldCountString.getOrNull(charIndex)
                val newChar = countString[charIndex]
                val char = if (oldChar == newChar) {
                    oldCountString[charIndex]
                } else {
                    countString[charIndex]
                }
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        transitionSpec(newChar, oldChar!!)
                    }) { char ->
                    Text(
                        text = char.toString(),
                        style = style,
                        softWrap = false,
                        fontSize = 35.sp
                    )
                }
            }
        }
    }
}

fun transitionSpec(newChar: Char, oldChar: Char): ContentTransform {
    if (newChar > oldChar) {
        return ContentTransform(
            targetContentEnter = slideInVertically { it },
            initialContentExit = slideOutVertically { -it }
        )
    }
    return ContentTransform(
        targetContentEnter = slideInVertically { -it },
        initialContentExit = slideOutVertically { it }
    )
}