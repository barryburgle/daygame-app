package com.barryburgle.gameapp.ui.input.dialog.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.utilities.button.LittleIconButton
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTextComponent(
    value: String,
    placeholder: String,
    emptyValue: String,
    validContent: Boolean,
    onCopyClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    var showDeleteTextDialog by remember { mutableStateOf(false) }
    if (showDeleteTextDialog) {
        DeleteConfirmationDialog(
            placeholder,
            "Do you want to delete your ${placeholder}?",
            onConfirmRequest = {
                onValueChange(emptyValue)
                showDeleteTextDialog = false
            },
            onDismissRequest = {
                showDeleteTextDialog = false
            },
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.07f)),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                TextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    placeholder = {
                        if (value.isEmpty()) {
                            WavyPlaceholder(text = "Type here your " + placeholder)
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            if (validContent) {
                Column(
                    modifier = Modifier.height(100.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    LittleIconButton(
                        onClick = {
                            showDeleteTextDialog = true
                        },
                        imageVector = Icons.Default.Delete,
                        height = 15.dp
                    )
                    LittleIconButton(
                        onClick = onCopyClick,
                        imageVector = Icons.Default.ContentCopy,
                        height = 15.dp
                    )
                }
            }
        }
    }
}

@Composable
fun WavyPlaceholder(text: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "WavyPlaceholderTransition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WavyPlaceholderProgress"
    )

    Row {
        text.forEachIndexed { index, char ->
            val charOffset = index * 0.08f
            val colorProgress = (progress * 3.5f - charOffset)
            val colorValue = ((sin(colorProgress * 2f * Math.PI.toFloat()) + 1f) / 2f)
            val animatedColor = lerp(
                MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                colorValue
            )
            Text(
                text = char.toString(),
                color = animatedColor,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}