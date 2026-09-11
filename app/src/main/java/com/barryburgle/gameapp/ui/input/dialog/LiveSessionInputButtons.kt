package com.barryburgle.gameapp.ui.input.dialog

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.ui.input.CounterColumn
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CounterColumn(
            count = setsCount,
            label = if (setsCount != 1) "Sets" else "Set",
            iconRes = R.drawable.set_action,
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
            count = convosCount,
            label = if (convosCount != 1) "Conversations" else "Conversation",
            iconRes = R.drawable.conversation_action,
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
            count = contactsCount,
            label = if (contactsCount != 1) "Contacts" else "Contact",
            iconRes = R.drawable.contact_action,
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
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pull",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "O'Clock",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
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
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
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
                    }
                    if (showRecordingButtons) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                                    }
                                }, onLongClick = {
                                    onTapRecordingDiscard(recordingState.activeFileName.orEmpty())
                                    // TODO: we should show a deletion confirmation dialog here as done for live session discard
                                    Toast.makeText(
                                        localContext,
                                        "Record discarded",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                                imageVector = if (recordingState.state == RecordingStateEnum.RECORDING) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                                contentDescription = "Record a set",
                                iconColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Record",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center
            )
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