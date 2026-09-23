package com.barryburgle.gameapp.ui.output.card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.ping.Ping
import com.barryburgle.gameapp.ui.tool.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText


// TODO: do all the lead sent tracking part
@Composable
fun PingCard(ping: Ping, onEvent: (OutputEvent) -> Unit) {
    val context = LocalContext.current
    val systemClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val fullTextToShare = buildString {
        if (!ping.body.isNullOrBlank()) {
            append("\n").append(ping.body)
        }
        if (!ping.link.isNullOrBlank()) {
            append("\n").append(ping.link)
        }
    }

    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onEvent(OutputEvent.EditPing(ping))
            }) {
        if (!ping.pic.isNullOrBlank()) {
            // TODO: support video linking and playback on the card background
            // TODO: support ig link -> cached thumbnail in card background (saved in a cache folder)
            AsyncImage(
                model = ping.pic,
                contentDescription = "Ping Pic",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    if (!ping.pic.isNullOrBlank()) {
                        Color.Black.copy(alpha = 0.45f)
                    } else {
                        Color.Transparent
                    }
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (ping.pic.isNullOrBlank()) MaterialTheme.colorScheme.surface else Color.Transparent)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    MediumTitleText(
                        text = ping.title, color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    WavyPlaceholder("Touch to edit")
                }
                if (!ping.body.isNullOrBlank() || !ping.link.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column {
                        if (!ping.body.isNullOrBlank()) {
                            LittleBodyText(
                                text = "\"" + ping.body + "\"",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        if (!ping.link.isNullOrBlank()) {
                            // TODO: link should be clickable & open default browser
                            Spacer(modifier = Modifier.height(2.dp))
                            LittleBodyText(
                                text = ping.link!!,
                                italic = true,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO: on each one of the copy/share button we should open a dialog with the list of leads ordered by last contact (if any)
                // or acquisition date and on their side a checkbox to select the ones we are sending that ping, to keep track
                IconShadowButton(
                    onClick = {
                        val clipData = if (!ping.pic.isNullOrBlank()) {
                            val imageUri = Uri.parse(ping.pic)
                            ClipData(
                                ping.title,
                                arrayOf("image/*", "text/plain", "text/html"),
                                ClipData.Item(imageUri)
                            ).apply {
                                if (fullTextToShare.isNotBlank()) {
                                    addItem(ClipData.Item(fullTextToShare))
                                }
                            }
                        } else {
                            ClipData.newPlainText(ping.title, fullTextToShare)
                        }

                        systemClipboard.setPrimaryClip(clipData)
                        Toast.makeText(context, "Ping copied to clipboard", Toast.LENGTH_SHORT)
                            .show()
                    },
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy to clipboard"
                )
                IconShadowButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            if (!ping.pic.isNullOrBlank()) {
                                val imageUri = Uri.parse(ping.pic)
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_STREAM, imageUri)
                                if (fullTextToShare.isNotBlank()) {
                                    putExtra(Intent.EXTRA_TEXT, fullTextToShare)
                                }
                                type = "image/*"
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            } else {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, fullTextToShare)
                                type = "text/plain"
                            }
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    },
                    imageVector = Icons.Default.Share,
                    contentDescription = "Send button (opens share menu)"
                )
            }
        }
    }
}