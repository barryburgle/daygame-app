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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    val fadingColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.surfaceVariant
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
            .padding(6.dp)
            .clip(cardShape)
            .background(textColor)
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
                .height(60.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            fadingColor.copy(alpha = 0.98f),
                            fadingColor.copy(alpha = 0.8f),
                            fadingColor.copy(alpha = 0.65f),
                            fadingColor.copy(alpha = 0.5f),
                            fadingColor.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    PillShapedTranslucent {
                        MediumTitleText(
                            text = ping.title, color = textColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    WavyPlaceholder("Touch to edit", color = textColor)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (ping.pic.isNullOrBlank()) MaterialTheme.colorScheme.surface else Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (!ping.body.isNullOrBlank() || !ping.link.isNullOrBlank()) {
                    Column {
                        if (!ping.body.isNullOrBlank()) {
                            LittleBodyText(
                                text = "\"" + ping.body + "\"",
                                color = textColor
                            )
                        }
                        if (!ping.link.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            LittleBodyText(
                                text = ping.link!!,
                                italic = true,
                                color = textColor,
                                modifier = Modifier.clickable {
                                    try {
                                        var url = ping.link!!
                                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                            url = "https://$url"
                                        }
                                        val browserIntent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(browserIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Cannot open link",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
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
                        val imageUri = ping.pic?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }

                        val clipData = if (imageUri != null) {
                            ClipData.newUri(context.contentResolver, ping.title, imageUri).apply {
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

@Composable
fun PillShapedTranslucent(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
        ) {
            content()
        }
    }
}