package com.barryburgle.gameapp.ui.utilities.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import java.time.Duration
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Timeline(
    abstractSession: AbstractSession,
    pinPoints: List<PinPoint>,
    leads: List<Lead>,
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

    var selectedPinPoint by remember { mutableStateOf<PinPoint?>(null) }
    var popupPositionX by remember { mutableStateOf(0f) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val circleRadiusPx = with(density) { 16.dp.toPx() }

    val clickableRegions =
        remember(pinPoints, sessionDuration) { mutableListOf<Pair<Offset, PinPoint>>() }

    Spacer(modifier = Modifier.height(10.dp))

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .pointerInput(pinPoints, sessionDuration) {
                        detectTapGestures { tapOffset ->
                            val match = clickableRegions.find { (centerOffset, _) ->
                                val distance = sqrt(
                                    (tapOffset.x - centerOffset.x).pow(2) + (tapOffset.y - centerOffset.y).pow(
                                        2
                                    )
                                )
                                distance <= circleRadiusPx
                            }
                            if (match != null) {
                                selectedPinPoint = match.second
                                popupPositionX = match.first.x
                            } else {
                                selectedPinPoint = null
                            }
                        }
                    }
            ) {
                val width = size.width
                val midY = size.height / 2

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

        // Custom Cloud Bubble Info Window Popup
        selectedPinPoint?.let { pin ->
            val associatedLead = leads.find { it.pinPointId == pin.id }

            // Build text dynamically matching MapDialog parameters
            val titleText = if (associatedLead != null) {
                "${associatedLead.name} ${CountryEnum.getFlagByAlpha3(associatedLead.nationality)} ${associatedLead.age}"
            } else {
                pin.pinPointType.replaceFirstChar { it.uppercase() }
            }

            val snippetText =
                FormatService.getDate(pin.localTimestamp.substring(0, 16) + 'Z') + " " +
                        FormatService.getTime(pin.localTimestamp.substring(0, 16) + 'Z')

            val subDescriptionText = if (associatedLead != null) {
                associatedLead.contact.replaceFirstChar { it.uppercase() }
            } else null

            // UI Layout Math adjustments for alignment anchors
            val xOffsetDp = with(density) { popupPositionX.toDp() } - 100.dp
            val yOffsetDp = (-70).dp

            Popup(
                offset = IntOffset(
                    with(density) { xOffsetDp.roundToPx() },
                    with(density) { yOffsetDp.roundToPx() }),
                onDismissRequest = { selectedPinPoint = null },
                properties = PopupProperties(focusable = true)
            ) {
                BubbleLayout(
                    title = titleText,
                    snippet = snippetText,
                    subDescription = subDescriptionText,
                    onDeleteClicked = { showDeleteConfirmDialog = true }
                )
            }
        }

        // Deletion Confirmation Target Dialog
        if (showDeleteConfirmDialog && selectedPinPoint != null) {

            AlertDialog(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.shadow(elevation = 10.dp),
                onDismissRequest = {
                    showDeleteConfirmDialog = false
                    selectedPinPoint = null
                },
                title = {
                    LargeTitleText(text = "Delete pinpoint")
                },
                text = {
                    LittleBodyText("Do you really want to delete this pinpoint? Once deleted you won't be able to insert it again")
                },
                confirmButton = {
                    ConfirmButton {
                        if (selectedPinPoint != null) {
                            onEvent(GameEvent.DeletePinPoint(selectedPinPoint!!))
                            showDeleteConfirmDialog = false
                            selectedPinPoint = null
                        }
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

@Composable
fun BubbleLayout(
    title: String,
    snippet: String,
    subDescription: String?,
    onDeleteClicked: () -> Unit
) {
    // Custom shape mimicking an InfoWindow speech-bubble / cloud
    val bubbleShape = remember {
        GenericShape { size, _ ->
            val arrowHeight = 24f
            val arrowWidth = 32f
            val cornerRadius = 32f

            // Draw main round-rect body layout bounded away from the lower cloud stem anchor area
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height - arrowHeight,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )
            // Center pointed cloud indicator stem
            moveTo(size.width / 2 - arrowWidth / 2, size.height - arrowHeight)
            lineTo(size.width / 2, size.height)
            lineTo(size.width / 2 + arrowWidth / 2, size.height - arrowHeight)
            close()
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .width(200.dp)
            .shadow(elevation = 6.dp, shape = bubbleShape)
            .background(color = MaterialTheme.colorScheme.background, shape = bubbleShape)
            .padding(bottom = 12.dp) // Offset room space calculations for arrow baseline
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = snippet,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                if (subDescription != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subDescription,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onDeleteClicked,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete pinpoint",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}