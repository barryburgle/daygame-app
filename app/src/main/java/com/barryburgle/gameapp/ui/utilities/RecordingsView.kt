package com.barryburgle.gameapp.ui.utilities

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.service.recording.RecordingService
// TODO: deleteEventConfirmationDialog and liveSessionPulsingColor are generic pieces that happen to
//  live in EventCard.kt / InputScreen.kt - cleaner would be to move them under ui/utilities/ so a
//  utility doesn't import from a screen or a card
import com.barryburgle.gameapp.ui.input.card.deleteEventConfirmationDialog
import com.barryburgle.gameapp.ui.input.liveSessionPulsingColor
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
@Preview
fun RecordingsView(
    recordingState: RecordingState = RecordingState(),
    recordings: List<String> = emptyList(),
    recordingsFolder: String = "",
    recordingsEnabled: Boolean = false,
    showRecordingButtons: Boolean = true,
    onTapRecordingStart: () -> Unit = {},
    onTapRecordingStop: () -> Unit = {},
    onTapRecordingDiscard: (String) -> Unit = {},
    onTapPlaybackPlay: (String) -> Unit = {},
    onTapPlaybackPause: () -> Unit = {},
    onTapRecordingDelete: (String) -> Unit = {},
    onSetPlaybackPosition: (Int) -> Unit = {}
) {
    if (!recordingsEnabled) return
    var pendingDeletion by remember { mutableStateOf<String?>(null) }
    var expandedRecordings by remember { mutableStateOf(emptySet<String>()) }
    pendingDeletion?.let { fileName ->
        deleteEventConfirmationDialog(
            "Recording",
            "Do you want to delete $fileName?",
            onConfirmRequest = {
                onTapRecordingDelete(fileName)
                pendingDeletion = null
            },
            onDismissRequest = { pendingDeletion = null }
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LittleBodyText("Recordings:")
        }
        // a finished session can only be played back, never recorded into
        if (showRecordingButtons) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {

                RecordingButton(
                    onClick = if (recordingState.state == RecordingStateEnum.RECORDING) onTapRecordingStop else onTapRecordingStart,
                    onLongClick = { onTapRecordingDiscard(recordingState.activeFileName.orEmpty()) },
                    imageVector = if (recordingState.state == RecordingStateEnum.RECORDING) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                    contentDescription = "Start recording",
                    enabled = true,
                    accent = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        recordings.forEach { recording ->
            val isThisPlaying =
                recordingState.state == RecordingStateEnum.PLAYING && recordingState.activeFileName == recording
            // the file the mic is writing right now: it can be looked at, not operated on
            val isBeingRecorded =
                recordingState.state.isRecording() && recordingState.activeFileName == recording
            val isExpanded = recording in expandedRecordings
            Spacer(modifier = Modifier.height(7.dp))
            // each recording is its own tinted block so the entries read as separate things
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(entryBackground())
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                // the whole header row is the toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isBeingRecorded)
                                Modifier
                            else
                                Modifier.clickable {
                                    expandedRecordings =
                                        if (isExpanded) expandedRecordings - recording
                                        else expandedRecordings + recording
                                }
                        )
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LittleBodyText(
                        text = recording.removeSuffix(RecordingService.RECORDING_FILE_EXTENSION),
                        modifier = Modifier.weight(1f),
                        color = if (isBeingRecorded) liveSessionPulsingColor()
                                else MaterialTheme.colorScheme.onPrimary
                    )
                    if (!isBeingRecorded) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Hide controls" else "Show controls",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.height(20.dp)
                        )
                    }
                }
                if (isExpanded && !isBeingRecorded) {
                    RecordingDetails(
                        recording = recording,
                        recordingsFolder = recordingsFolder,
                        // only the loaded recording has a position: every other bar is inert
                        isActive = recordingState.state.isPlaying() && recordingState.activeFileName == recording,
                        isThisPlaying = isThisPlaying,
                        isRecording = recordingState.state.isRecording(),
                        onTapPlaybackPlay = { onTapPlaybackPlay(recording) },
                        onTapPlaybackPause = onTapPlaybackPause,
//                        onTapDelete = { pendingDeletion = recording },
                        onSeek = onSetPlaybackPosition
                    )
                }
            }
        }
    }

}

@Composable
private fun RecordingDetails(
    recording: String,
    recordingsFolder: String,
    isActive: Boolean,
    isThisPlaying: Boolean,
    isRecording: Boolean,
    onTapPlaybackPlay: () -> Unit,
    onTapPlaybackPause: () -> Unit,
    onSeek: (Int) -> Unit
) {
    // the player only knows the duration of the file it has loaded, so a row that isn't playing
    // reads its own once, off the main thread, to have a total to show and a bar to scale
    var fileDurationMs by remember(recording) { mutableIntStateOf(0) }
    LaunchedEffect(recording, recordingsFolder) {
        fileDurationMs = withContext(Dispatchers.IO) { RecordingService.durationOf(recordingsFolder, recording) }
    }
    Spacer(modifier = Modifier.height(4.dp))
    RecordingProgressBar(isActive, fileDurationMs, onSeek)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RecordingButton(
            onClick = { if (isThisPlaying) onTapPlaybackPause() else onTapPlaybackPlay() },
            onLongClick = { if (isThisPlaying) onTapPlaybackPause() else onTapPlaybackPlay() },
            imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isThisPlaying) "Pause playback" else "Play recording",
            enabled = !isRecording
        )
        Spacer(modifier = Modifier.weight(1f))
        PlaybackTimes(isActive, fileDurationMs)
    }
}

@Composable
private fun PlaybackTimes(isActive: Boolean, fileDurationMs: Int) {
    // collected here rather than in the parent so the buttons beside it don't recompose per tick
    val progress by RecordingService.playbackProgress.collectAsState()
    val positionMs = if (isActive) progress.positionMs else 0
    val durationMs = if (isActive && progress.durationMs > 0) progress.durationMs else fileDurationMs
    LittleBodyText("${FormatService.getDuration(positionMs)} / ${FormatService.getDuration(durationMs)}")
}

// the thumb/track slots that let the default tall pill thumb be replaced are still experimental
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordingProgressBar(isActive: Boolean, fileDurationMs: Int, onSeek: (Int) -> Unit) {
    // collected here rather than passed down through InputState: the position ticks a few times a
    // second, and this way only this composable recomposes, not the row, the card or the screen
    val progress by RecordingService.playbackProgress.collectAsState()
    // while dragging, the thumb follows the finger instead of fighting the ticker
    var scrubFraction by remember { mutableStateOf<Float?>(null) }
    // the file's own duration keeps an idle bar full width; only the loaded one has a position
    val durationMs = if (isActive && progress.durationMs > 0) progress.durationMs else fileDurationMs
    val positionMs = if (isActive) progress.positionMs else 0
    // onPrimary is what LittleBodyText draws the row label with, so it is the one color guaranteed
    // to read against the card in both light and dark mode
    val barColor = MaterialTheme.colorScheme.onPrimary
    val sliderColors = SliderDefaults.colors(
        thumbColor = barColor,
        activeTrackColor = barColor,
        inactiveTrackColor = barColor.copy(alpha = 0.25f),
        disabledThumbColor = barColor.copy(alpha = 0.4f),
        disabledActiveTrackColor = barColor.copy(alpha = 0.15f),
        disabledInactiveTrackColor = barColor.copy(alpha = 0.15f)
    )
    Slider(
        // coerced: currentPosition can briefly overshoot duration at the end of a clip
        value = (scrubFraction
            ?: if (durationMs > 0) positionMs.toFloat() / durationMs else 0f)
            .coerceIn(0f, 1f),
        onValueChange = { scrubFraction = it },
        onValueChangeFinished = {
            scrubFraction?.let { onSeek((it * durationMs).toInt()) }
            scrubFraction = null
        },
        enabled = isActive && durationMs > 0,
        colors = sliderColors,
        // default thumb is too tall, so we add a small round one that suits a card row better
        // the thumb is boxed to TRACK_BOX_HEIGHT because the slider top-aligns the thumb and track
        thumb = {
            Box(
                modifier = Modifier.size(width = THUMB_SIZE, height = TRACK_BOX_HEIGHT),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(THUMB_SIZE)
                        .background(
                            color = if (isActive && durationMs > 0) barColor else barColor.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                )
            }
        },
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                enabled = isActive && durationMs > 0,
                colors = sliderColors,
                modifier = Modifier.height(TRACK_HEIGHT),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = 0.dp,
                drawStopIndicator = null
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

// nudged away from the card colour rather than picked from a token: onSurface is dark on a light
// palette and light on a dark one, so this separates the entry from the card either way
@Composable
private fun entryBackground(): Color = lerp(
    MaterialTheme.colorScheme.surface,
    MaterialTheme.colorScheme.onSurface,
    ENTRY_BACKGROUND_BLEND
)

@Composable
private fun RecordingButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    accent: Color? = null // null = the default tint
) {
    val base = accent ?: MaterialTheme.colorScheme.inversePrimary
    val container = MaterialTheme.colorScheme.tertiary
    IconShadowButton(
        onClick = onClick,
        onLongClick = onLongClick,
        imageVector = imageVector,
        contentDescription = if (enabled) contentDescription else "$contentDescription (unavailable)",
        // faded towards the card colour
        color = if (enabled) container
                else lerp(container, MaterialTheme.colorScheme.surface, DISABLED_CONTAINER_BLEND),
        iconColor = if (enabled) base else base.copy(alpha = DISABLED_ICON_ALPHA)
    )
}

private val THUMB_SIZE = 12.dp
private val TRACK_HEIGHT = 4.dp

private const val ENTRY_BACKGROUND_BLEND = 0.08f // how far an entry sits off the card colour
private const val DISABLED_CONTAINER_BLEND = 0.7f // how far the container fades towards the card
private const val DISABLED_ICON_ALPHA = 0.38f

// the thumb the track height to end up centred on the line
private val TRACK_BOX_HEIGHT = 16.dp
