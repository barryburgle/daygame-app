package com.barryburgle.gameapp.ui.tool.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DiscreteLevelSlider(
    currentValue: Int,
    valueRange: IntRange,
    labels: List<String> = emptyList(),
    onValueChange: (Int) -> Unit
) {
    val steps = (valueRange.endInclusive - valueRange.start) - 1

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
    ) {
        Slider(
            value = currentValue.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = valueRange.start.toFloat()..valueRange.endInclusive.toFloat(),
            steps = if (steps > 0) steps else 0,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.24f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (labels.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                valueRange.forEach { index ->
                    val label = labels.getOrNull(index - valueRange.start) ?: index.toString()
                    Text(
                        text = label,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        style = if (index == currentValue) {
                            MaterialTheme.typography.titleSmall
                        } else {
                            MaterialTheme.typography.bodySmall
                        },
                        color = if (index == currentValue) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                        }
                    )
                }
            }
        }
    }
}