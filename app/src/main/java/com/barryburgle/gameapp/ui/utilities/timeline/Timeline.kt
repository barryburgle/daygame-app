package com.barryburgle.gameapp.ui.utilities.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import java.time.Duration
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Timeline(
    abstractSession: AbstractSession,
    pinPoints: List<PinPoint>,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val setPainter = rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.set_action))
    val convoPainter =
        rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.conversation_action))
    val contactPainter =
        rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.contact_action))
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer

    val startTime = FormatService.parseTime(abstractSession.startHour)
    val sessionDuration = abstractSession.sessionTime

    // Dialog state management
    var selectedPinPoint by remember { mutableStateOf<PinPoint?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Dynamic list mapping coordinate layouts to pinpoints for touch matching
    val density = LocalDensity.current
    val circleRadiusPx = with(density) { 16.dp.toPx() }
    val clickableRegions =
        remember(pinPoints, sessionDuration) { mutableListOf<Pair<Offset, PinPoint>>() }

    Spacer(modifier = Modifier.height(10.dp))

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // Height increased slightly to provide a safer tap/touch target
                .padding(horizontal = 16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .pointerInput(pinPoints, sessionDuration) {
                        detectTapGestures { tapOffset ->
                            // Look for the closest pinpoint center intersecting the user click bounds
                            val clickedItem = clickableRegions.find { (centerOffset, _) ->
                                val distance = sqrt(
                                    (tapOffset.x - centerOffset.x).pow(2) + (tapOffset.y - centerOffset.y).pow(
                                        2
                                    )
                                )
                                distance <= circleRadiusPx
                            }?.second

                            if (clickedItem != null) {
                                selectedPinPoint = clickedItem
                            }
                        }
                    }
            ) {
                val width = size.width
                val midY = size.height / 2

                // Clear regions to prevent stale mapping coordinates during re-draws
                clickableRegions.clear()

                drawLine(
                    color = primaryContainerColor,
                    start = Offset(0f, midY),
                    end = Offset(width, midY),
                    strokeWidth = 10.dp.toPx(),
                    cap = StrokeCap.Round
                )

                pinPoints.forEach { pin ->
                    val pinTime = FormatService.parseTime(pin.localTimestamp.substring(0, 16) + 'Z')
                    if (sessionDuration > 0) {
                        val elapsedMinutes = Duration.between(startTime, pinTime).toMinutes()
                        val ratio =
                            (elapsedMinutes.toFloat() / sessionDuration.toFloat()).coerceIn(0f, 1f)
                        val xPos = width * ratio
                        val centerPoint = Offset(xPos, midY)

                        val painter = when (pin.pinPointType.lowercase()) {
                            PinPointTypeEnum.SET.getField() -> setPainter
                            PinPointTypeEnum.CONVERSATION.getField() -> convoPainter
                            PinPointTypeEnum.CONTACT.getField() -> contactPainter
                            else -> null
                        }

                        painter?.let {
                            // Map coordinates to register tap zones later
                            clickableRegions.add(centerPoint to pin)

                            val iconSize = 24.dp.toPx()
                            drawCircle(
                                color = Color.White,
                                radius = circleRadiusPx,
                                center = centerPoint
                            )
                            translate(
                                left = xPos - (iconSize / 2),
                                top = midY - (iconSize / 2)
                            ) {
                                with(it) {
                                    draw(size = Size(iconSize, iconSize))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Details Info Window Popup Dialog
        selectedPinPoint?.let { pin ->
            val formattedTime = FormatService.getTime(pin.localTimestamp.substring(0, 16) + "Z")
            val formattedDate = FormatService.getDate(pin.localTimestamp.substring(0, 16) + "Z")

            AlertDialog(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.shadow(elevation = 10.dp),
                onDismissRequest = { selectedPinPoint = null },
                title = {
                    LargeTitleText(text = pin.pinPointType.replaceFirstChar { it.uppercase() })
                },
                text = {
                    LittleBodyText(text = "Registered at: $formattedDate $formattedTime")
                },
                confirmButton = {
                    ConfirmButton(onClick = { selectedPinPoint = null })
                },
                dismissButton = {
                    // Leverages gameapp's global styling standard for operational context switching
                    DismissButton(
                        text = "Delete",
                        textColor = MaterialTheme.colorScheme.error,
                        onClick = { showDeleteConfirmDialog = true }
                    )
                }
            )
        }

        // Deletion Confirmation Target Dialog
        if (showDeleteConfirmDialog && selectedPinPoint != null) {
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.shadow(elevation = 10.dp),
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = {
                    LargeTitleText(text = "Delete pinpoint")
                },
                text = {
                    LittleBodyText("Do you really want to delete this pinpoint? Once deleted you won't be able to insert it again")
                },
                confirmButton = {
                    ConfirmButton {
                        selectedPinPoint?.let { pin ->
                            onEvent(GameEvent.DeletePinPoint(pin))
                        }
                        showDeleteConfirmDialog = false
                        selectedPinPoint = null // Close both windows upon execution
                    }
                },
                dismissButton = {
                    DismissButton {
                        showDeleteConfirmDialog = false
                    }
                }
            )
        }
    }
}

// Inline fallback wrappers assumed matching GameApp's standard custom wrapper patterns
@Composable
private fun ConfirmButton(onClick: () -> Unit) {
    androidx.compose.material3.TextButton(onClick = onClick) {
        androidx.compose.material3.Text("OK", color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun DismissButton(
    text: String = "Cancel",
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    androidx.compose.material3.TextButton(onClick = onClick) {
        androidx.compose.material3.Text(text, color = textColor)
    }
}