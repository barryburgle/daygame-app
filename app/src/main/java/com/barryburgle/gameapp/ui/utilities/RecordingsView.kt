package com.barryburgle.gameapp.ui.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Voicemail
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.service.recording.RecordingService
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
// TODO: deleteEventConfirmationDialog and liveSessionPulsingColor are generic pieces that happen to
//  live in EventCard.kt / InputScreen.kt - cleaner would be to move them under ui/utilities/ so a
//  utility doesn't import from a screen or a card
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.body.MediumBodyText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sin

@Composable
@Preview
fun RecordingsView(
    recordingState: RecordingState = RecordingState(),
    recordings: List<String> = emptyList(),
    recordingsFolder: String = "",
    recordingsEnabled: Boolean = false,
    onTapPlaybackPlay: (String) -> Unit = {},
    onTapPlaybackPause: () -> Unit = {},
    onTapRecordingDelete: (String) -> Unit = {},
    onSetPlaybackPosition: (Int) -> Unit = {}
) {
    val NO_RECORDING_AVAILABLE = "No recordings available"
    if (!recordingsEnabled) return
    var pendingDeletion by remember { mutableStateOf<String?>(null) }
    pendingDeletion?.let { fileName ->
        DeleteConfirmationDialog(
            "Recording",
            "Do you want to delete $fileName?",
            onConfirmRequest = {
                onTapRecordingDelete(fileName)
                pendingDeletion = null
            },
            onDismissRequest = { pendingDeletion = null }
        )
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedRecording by remember(recordings) {
        mutableStateOf(recordingState.activeFileName.takeIf { it in recordings }
            ?: recordings.firstOrNull())
    }

    if (selectedRecording !in recordings) {
        selectedRecording = recordings.firstOrNull()
    }

    val currentRecording = selectedRecording
    val isThisPlaying =
        recordingState.state == RecordingStateEnum.PLAYING && recordingState.activeFileName == currentRecording

    // Local component progress and dragging state
    var localProgress by remember(currentRecording) { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    // Read recording duration for progress calculation
    var fileDurationMs by remember(currentRecording) { mutableIntStateOf(0) }
    LaunchedEffect(currentRecording, recordingsFolder) {
        if (currentRecording != null && recordingsFolder.isNotEmpty()) {
            fileDurationMs = withContext(Dispatchers.IO) {
                RecordingService.durationOf(recordingsFolder, currentRecording)
            }
        }
    }

    // Auto-advance progress cursor while playing
    LaunchedEffect(isThisPlaying, isDragging, currentRecording) {
        if (isThisPlaying && !isDragging) {
            val totalDuration = if (fileDurationMs > 0) fileDurationMs.toFloat() else 10000f
            val stepIntervalMs = 50L
            if (localProgress >= 1f) {
                localProgress = 0f
            }
            while (isThisPlaying && !isDragging && localProgress < 1f) {
                delay(stepIntervalMs)
                localProgress = (localProgress + (stepIntervalMs / totalDuration)).coerceAtMost(1f)
            }
            if (localProgress >= 1f) {
                onTapPlaybackPause()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentRecording != null) {
                IconShadowButton(
                    onClick = { pendingDeletion = currentRecording },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete recording",
                    iconColor = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(entryBackground())
                    .clickable { dropdownExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val filename =
                        currentRecording?.removeSuffix(RecordingService.RECORDING_FILE_EXTENSION)
                            ?.let { name ->
                                if (name.length > 15) "${name.take(5)} ... ${name.takeLast(6)}" else name
                            } ?: NO_RECORDING_AVAILABLE
                    Row(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Default.Voicemail,
                            contentDescription = "Recordings",
                            modifier = Modifier
                                .height(25.dp)
                        )
                        MediumBodyText(filename)
                    }
                    if (filename != NO_RECORDING_AVAILABLE) {
                        Spacer(modifier = Modifier.height(5.dp))
                        WavyPlaceholder("Tap to select recordings")
                    }
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(entryBackground())
                ) {
                    if (recordings.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text(NO_RECORDING_AVAILABLE) },
                            onClick = { dropdownExpanded = false },
                            enabled = false
                        )
                    } else {
                        recordings.forEach { recording ->
                            val cleanName =
                                recording.removeSuffix(RecordingService.RECORDING_FILE_EXTENSION)
                            val isSelected = recording == currentRecording
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = cleanName,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                                onClick = {
                                    selectedRecording = recording
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            if (currentRecording != null) {
                Spacer(modifier = Modifier.width(12.dp))
                IconShadowButton(
                    onClick = {
                        if (isThisPlaying) {
                            onTapPlaybackPause()
                        } else {
                            if (localProgress >= 1f) localProgress = 0f
                            onTapPlaybackPlay(currentRecording)
                        }
                    },
                    imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                )
            }
        }
        AnimatedVisibility(
            visible = isThisPlaying,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val totalDurationMs = if (fileDurationMs > 0) fileDurationMs else 10000
            val currentMs = (localProgress * totalDurationMs).toInt()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconShadowButton(
                        onClick = {
                            val targetMs = (currentMs - 5000).coerceAtLeast(0)
                            localProgress = targetMs.toFloat() / totalDurationMs
                            onSetPlaybackPosition(targetMs)
                        },
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Rewind 5 seconds"
                    )
                    WavyProgressSlider(
                        value = localProgress,
                        onValueChange = { newProgress ->
                            isDragging = true
                            localProgress = newProgress
                        },
                        onValueChangeFinished = {
                            isDragging = false
                            val targetMs = (localProgress * totalDurationMs).toInt()
                            onSetPlaybackPosition(targetMs)
                        },
                        modifier = Modifier.weight(1f),
                        currentMs = currentMs,
                        totalDurationMs = totalDurationMs
                    )
                    IconShadowButton(
                        onClick = {
                            val targetMs = (currentMs + 5000).coerceAtMost(totalDurationMs)
                            localProgress = targetMs.toFloat() / totalDurationMs
                            onSetPlaybackPosition(targetMs)
                        },
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Forward 5 seconds"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WavyProgressSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onTertiary,
    trackColor: Color = MaterialTheme.colorScheme.primary,
    currentMs: Int,
    totalDurationMs: Int
) {
    // Continuous wave phase shift animation
    val infiniteTransition = rememberInfiniteTransition(label = "waveAnimation")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    Column(modifier = modifier) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            modifier = Modifier.fillMaxWidth(),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(color, CircleShape)
                )
            },
            track = { sliderState ->
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2f
                    val activeWidth = width * sliderState.value.coerceIn(0f, 1f)

                    // Inactive track line
                    drawLine(
                        color = trackColor,
                        start = Offset(activeWidth, centerY),
                        end = Offset(width, centerY),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Moving wave track
                    if (activeWidth > 0f) {
                        val wavePath = Path()
                        val waveLength = 16.dp.toPx()
                        val amplitude = 3.dp.toPx()

                        wavePath.moveTo(0f, centerY + sin(phase).toFloat() * amplitude)
                        var x = 0f
                        while (x <= activeWidth) {
                            val y =
                                centerY + sin((x / waveLength) * 2 * Math.PI + phase).toFloat() * amplitude
                            wavePath.lineTo(x, y)
                            x += 2f
                        }

                        drawPath(
                            path = wavePath,
                            color = color.copy(alpha = 0.75f),
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LittleBodyText(
                formatTimeMs(currentMs)
            )
            LittleBodyText(
                formatTimeMs(totalDurationMs)
            )
        }
    }
}

private fun formatTimeMs(ms: Int): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

@Composable
private fun entryBackground(): Color = lerp(
    MaterialTheme.colorScheme.surface,
    MaterialTheme.colorScheme.onSurface,
    ENTRY_BACKGROUND_BLEND
)

private const val ENTRY_BACKGROUND_BLEND = 0.08f