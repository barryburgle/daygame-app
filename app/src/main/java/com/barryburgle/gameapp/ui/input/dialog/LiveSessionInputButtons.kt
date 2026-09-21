package com.barryburgle.gameapp.ui.input.dialog

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.DaygameDiceEnum
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.ui.input.CounterColumn
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun LiveSessionInputButtons(
    onSetsChange: (Int) -> Unit,
    onConvosChange: (Int, Boolean) -> Unit,
    onContactsChange: (Int, Boolean) -> Unit,
    setsCount: Int,
    convosCount: Int,
    contactsCount: Int,
    liveSessionShareEnabled: Boolean,
    copyReportOnClipboard: Boolean,
    onEvent: (GameEvent) -> Unit,
    pullOClockReminderInterval: Int,
    recordingState: RecordingState = RecordingState(),
    showRecordingButtons: Boolean = true,
    onTapRecordingStart: () -> Unit = {},
    onTapRecordingStop: () -> Unit = {},
    onTapRecordingDiscard: (String) -> Unit = {}
) {
    // TODO: integrate this in session dialog: it works but only on the backend, leaving stale unchanged values on the dialog
    val localContext = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var pendingDiscarding by remember { mutableStateOf<String?>(null) }
    pendingDiscarding?.let { fileName ->
        DeleteConfirmationDialog(
            "Recording",
            "Do you want to discard the ongoing recording?",
            onConfirmRequest = {
                onTapRecordingDiscard(recordingState.activeFileName.orEmpty())
                Toast.makeText(
                    localContext,
                    "Record discarded",
                    Toast.LENGTH_SHORT
                )
                    .show()
                pendingDiscarding = null
            },
            onDismissRequest = { pendingDiscarding = null }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val plusButtonSecondImageVector =
            if (recordingState.stopRecordingOnNewEntryEnable && recordingState.state == RecordingStateEnum.RECORDING) Icons.Default.Stop else null
        CounterColumn(
            count = setsCount.toString(),
            label = if (setsCount != 1) "Sets" else "Set",
            iconRes = R.drawable.set_action,
            plusButtonSecondImageVector = plusButtonSecondImageVector,
            onIncrement = {
                onSetsChange(setsCount + 1)
                shareEvent(
                    liveSessionShareEnabled,
                    copyReportOnClipboard,
                    "set",
                    "\uD83C\uDFC3",
                    localContext,
                    clipboardManager
                )
            },
            onDecrement = { onSetsChange(setsCount - 1) }
        )
        CounterColumn(
            count = convosCount.toString(),
            label = if (convosCount != 1) "Conversations" else "Conversation",
            iconRes = R.drawable.conversation_action,
            plusButtonSecondImageVector = plusButtonSecondImageVector,
            onIncrement = {
                onConvosChange(convosCount + 1, true)
                shareEvent(
                    liveSessionShareEnabled,
                    copyReportOnClipboard,
                    "conversation",
                    "\uD83D\uDCAC",
                    localContext,
                    clipboardManager
                )
            },
            onDecrement = { onConvosChange(convosCount - 1, false) }
        )
        CounterColumn(
            count = contactsCount.toString(),
            label = if (contactsCount != 1) "Contacts" else "Contact",
            iconRes = R.drawable.contact_action,
            plusButtonSecondImageVector = plusButtonSecondImageVector,
            onIncrement = {
                onContactsChange(contactsCount + 1, true)
                shareEvent(
                    liveSessionShareEnabled,
                    copyReportOnClipboard,
                    "contact",
                    "\uD83D\uDCF2",
                    localContext,
                    clipboardManager
                )
            },
            onDecrement = { onContactsChange(contactsCount - 1, false) }
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .let {
                            if (showRecordingButtons) it.fillMaxHeight() else it.fillMaxHeight(
                                0.35f
                            )
                        }
                        .background(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (showRecordingButtons) Arrangement.SpaceBetween else Arrangement.Center
                    ) {
                        IconShadowButton(
                            onClick =
                                {
                                    onEvent(
                                        GameEvent.SchedulePullOClockReminder(
                                            pullOClockReminderInterval
                                        )
                                    )
                                    Toast.makeText(
                                        localContext,
                                        "Pull reminder set",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = "Pull O'Clock"
                        )
                        IconShadowButton(
                            onClick =
                                {
                                    val extracted = DaygameDiceEnum.random()
                                    Toast.makeText(
                                        localContext,
                                        "${extracted.value} - ${extracted.description}",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Daygame Dice"
                        )
                        if (showRecordingButtons) {
                            IconShadowButton(
                                onClick = {
                                    if (recordingState.state == RecordingStateEnum.RECORDING) {
                                        onTapRecordingStop()
                                        Toast.makeText(
                                            localContext,
                                            "Recording stopped",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onTapRecordingStart()
                                        Toast.makeText(
                                            localContext,
                                            "Recording started",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (recordingState.triggerPullOClockWithRecordingsEnable) {
                                            Toast.makeText(
                                                localContext,
                                                "Pull reminder set",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }, onLongClick = {
                                    pendingDiscarding = recordingState.activeFileName
                                },
                                imageVector = if (recordingState.state == RecordingStateEnum.RECORDING) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                                modifier = Modifier.scale(1.5f),
                                contentDescription = "Record a set",
                                iconColor = if (recordingState.state == RecordingStateEnum.RECORDING) null else MaterialTheme.colorScheme.onErrorContainer,
                                secondaryImageVector = if (recordingState.triggerPullOClockWithRecordingsEnable) Icons.Default.Timelapse else null
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LittleBodyText(
                    text = "Pull O'Clock",
                    modifier = Modifier
                        .vertical()
                        .rotate(-90f)
                )
                if (showRecordingButtons) {
                    var recordDesc = "Record"
                    if (recordingState.triggerPullOClockWithRecordingsEnable) {
                        recordDesc += " & Pull"
                    }
                    LittleBodyText(
                        text = recordDesc,
                        modifier = Modifier
                            .vertical()
                            .rotate(-90f)
                    )
                }
            }
        }
    }
}

fun shareEvent(
    liveSessionShareEnabled: Boolean,
    copyReportOnClipboard: Boolean,
    eventType: String,
    eventEmoji: String,
    localContext: Context,
    clipboardManager: ClipboardManager
) {
    if (liveSessionShareEnabled) {
        val eventReport = "%s New %s done!".format(eventEmoji, eventType)
        if (copyReportOnClipboard) {
            clipboardManager.setText(
                AnnotatedString(
                    eventReport
                )
            )
            Toast.makeText(
                localContext,
                "Event copied",
                Toast.LENGTH_SHORT
            ).show()
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                eventReport
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(
            sendIntent,
            "Share report"
        )
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localContext.startActivity(shareIntent)
    }
}

fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}