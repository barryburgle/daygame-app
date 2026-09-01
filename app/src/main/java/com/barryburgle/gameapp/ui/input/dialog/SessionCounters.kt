package com.barryburgle.gameapp.ui.input.dialog

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier

@Composable
fun SessionCounters(
    onSetsChange: (Int) -> Unit,
    onConvosChange: (Int, Boolean) -> Unit,
    onContactsChange: (Int, Boolean) -> Unit,
    setsCount: Int,
    convosCount: Int,
    contactsCount: Int,
    liveSessionShareEnabled: Boolean,
    copyReportOnClipboard: Boolean,
    onEvent: (GameEvent) -> Unit,
    pullOClockReminderInterval: Int
) {
    // TODO: integrate this in session dialog: it works but only on the backend, leaving stale unchanged values on the dialog
    val localContext = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Row(
        modifier = Modifier.fillMaxWidth(),
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconShadowButton(
                onClick =
                    {
                        onEvent(
                            GameEvent.SchedulePullOClockReminder(
                                pullOClockReminderInterval
                            )
                        )
                        Toast.makeText(localContext, "Pull reminder set!", Toast.LENGTH_SHORT)
                            .show()
                    },
                imageVector = Icons.Default.Timelapse,
                contentDescription = "Pull O'Clock"
            )
            // TODO: add here recoding buttons for live Session Recording
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

@Composable
fun CounterColumn(
    count: Int,
    label: String,
    @DrawableRes iconRes: Int? = null,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconShadowButton(
            onClick = onDecrement,
            imageVector = Icons.Default.Remove,
            contentDescription = "Less"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            DescribedQuantifier(
                quantity = count.toString(),
                quantityFontSize = 50.sp,
                description = label,
                descriptionFontSize = 10.sp,
                drawableIcon = iconRes
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        IconShadowButton(
            onClick = onIncrement,
            imageVector = Icons.Default.Add,
            contentDescription = "More"
        )
    }
}