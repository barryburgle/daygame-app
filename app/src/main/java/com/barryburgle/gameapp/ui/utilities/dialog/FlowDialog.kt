package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val semiOpaqueBackground = MaterialTheme.colorScheme.surfaceVariant

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = semiOpaqueBackground)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                content(PaddingValues(top = 70.dp, bottom = 60.dp, start = 16.dp, end = 16.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    semiOpaqueBackground,
                                    semiOpaqueBackground.copy(alpha = 0.85f),
                                    semiOpaqueBackground.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    title()
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    semiOpaqueBackground,
                                    semiOpaqueBackground.copy(alpha = 0.85f),
                                    semiOpaqueBackground.copy(alpha = 0.4f),
                                    Color.Transparent
                                ), startY = Float.POSITIVE_INFINITY, endY = 0f
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        ConfirmButton(onClick = onConfirm)
                    }
                }
            }
        }
    }
}