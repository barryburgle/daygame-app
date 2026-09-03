package com.barryburgle.gameapp.ui.input.dialog.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
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
    emptyValue: String = "",
    singleLine: Boolean = true,
    onCopyClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    val validContent = !value.isBlank()
    val onPrimaryColor: Color = MaterialTheme.colorScheme.onPrimary
    val backgroundColor: Color = MaterialTheme.colorScheme.onBackground
    val containerColor = onPrimaryColor.copy(alpha = 0.07f)
    val opaqueContainerColor =
        lerp(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), onPrimaryColor, 0.07f)
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(containerColor)
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                if (value.isEmpty()) {
                    WavyPlaceholder(text = "Type here your " + placeholder)
                }
            },
            singleLine = singleLine,
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
                .then(
                    if (validContent) {
                        Modifier.drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    0.85f to Color.Transparent,
                                    1.0f to opaqueContainerColor
                                )
                            )
                        }
                    } else Modifier
                )
        )
        if (validContent) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                LittleIconButton(
                    onClick = {
                        showDeleteTextDialog = true
                    },
                    imageVector = Icons.Default.Delete,
                    height = 15.dp,
                    color = backgroundColor
                )
                if (onCopyClick != null && !singleLine) {
                    LittleIconButton(
                        onClick = onCopyClick,
                        imageVector = Icons.Default.ContentCopy,
                        height = 15.dp,
                        color = backgroundColor
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