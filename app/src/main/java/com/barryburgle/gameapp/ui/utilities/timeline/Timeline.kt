package com.barryburgle.gameapp.ui.utilities.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.FormatService
import java.time.Duration

@Composable
fun Timeline(
    abstractSession: AbstractSession,
    pinPoints: List<PinPoint>,
    modifier: Modifier = Modifier
) {
    val setPainter = rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.set_action))
    val convoPainter =
        rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.conversation_action))
    val contactPainter =
        rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.contact_action))
    val startTime = FormatService.parseTime(abstractSession.startHour)
    val sessionDuration = abstractSession.sessionTime

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val width = size.width
            val midY = size.height / 2
            drawLine(
                color = Color.Gray,
                start = Offset(0f, midY),
                end = Offset(width, midY),
                strokeWidth = 10.dp.toPx()
            )
            pinPoints.forEach { pin ->
                val pinTime = FormatService.parseTime(pin.localTimestamp.substring(0, 16) + 'Z')
                if (sessionDuration > 0) {
                    val elapsedMinutes = Duration.between(startTime, pinTime).toMinutes()
                    val ratio =
                        (elapsedMinutes.toFloat() / sessionDuration.toFloat()).coerceIn(0f, 1f)
                    val xPos = width * ratio
                    val painter = when (pin.pinPointType.lowercase()) {
                        PinPointTypeEnum.SET.getField() -> setPainter
                        PinPointTypeEnum.CONVERSATION.getField() -> convoPainter
                        PinPointTypeEnum.CONTACT.getField() -> contactPainter
                        else -> null
                    }
                    painter?.let {
                        val iconSize = 24.dp.toPx()
                        translate(
                            left = xPos - (iconSize / 2),
                            top = midY - iconSize - 4.dp.toPx()
                        ) {
                            with(it) {
                                draw(size = Size(iconSize, iconSize))
                            }
                        }
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = Offset(xPos, midY)
                        )
                    }
                }
            }
        }
    }
}