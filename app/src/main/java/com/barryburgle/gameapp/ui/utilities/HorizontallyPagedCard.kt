package com.barryburgle.gameapp.ui.utilities

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText

@Composable
fun HorizontallyPagedCard(
    title: String,
    description: String? = null,
    clickableLink: String? = null,
    backgroundMediaLocalUrl: String? = null,
    firstActionButtonIcon: ImageVector,
    firstActionButtonIconGlowing: Boolean = false,
    onFirstActionButtonClick: () -> Unit,
    onShareActionButtonClick: () -> Unit,
    onTouchToEditClick: () -> Unit
) {
    val textColor =
        if (backgroundMediaLocalUrl != null) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onPrimary
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp, shape = MaterialTheme.shapes.large
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    onTouchToEditClick()
                }
        ) {
            if (!backgroundMediaLocalUrl.isNullOrBlank()) {
                // TODO: support video linking and playback on the card background
                // TODO: support ig link -> cached thumbnail in card background (saved in a cache folder)
                AsyncImage(
                    model = backgroundMediaLocalUrl,
                    contentDescription = "Ping Pic",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        if (!backgroundMediaLocalUrl.isNullOrBlank()) {
                            Color.Black.copy(alpha = 0.45f)
                        } else {
                            Color.Transparent
                        }
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PillShapedTranslucent {
                            MediumTitleText(
                                text = title,
                                color = textColor
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        WavyPlaceholder("Touch to edit", color = textColor.copy(alpha = 0.5f))
                    }
                    if (!description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LittleBodyText(
                            text = "\"$description\"",
                            color = textColor
                        )
                    }

                    if (!clickableLink.isNullOrBlank()) {
                        Row(
                            modifier = Modifier
                                .background(Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable {
                                    try {
                                        var url = clickableLink
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
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = "Link icon",
                                tint = textColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            LittleBodyText(
                                text = clickableLink,
                                italic = true,
                                color = textColor
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconShadowButton(
                        onClick = {
                            onFirstActionButtonClick()
                        },
                        imageVector = firstActionButtonIcon,
                        contentDescription = "Copy to clipboard",
                        glowing = firstActionButtonIconGlowing
                    )
                    IconShadowButton(
                        onClick = {
                            onShareActionButtonClick()
                        },
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share menu"
                    )
                }
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
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp)
    ) {
        content()
    }
}

fun getShareableText(
    description: String?,
    clickableLink: String?
) = buildString {
    if (!description.isNullOrBlank()) {
        append(description)
    }
    if (!clickableLink.isNullOrBlank()) {
        if (isNotEmpty()) append("\n")
        append(clickableLink)
    }
}


fun getClipData(
    context: Context,
    title: String,
    description: String?,
    clickableLink: String?,
    backgroundMediaLocalUrl: String?
): ClipData {
    val shareableText = getShareableText(
        description,
        clickableLink
    )
    val imageUri = backgroundMediaLocalUrl?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
    return if (imageUri != null) {
        ClipData.newUri(context.contentResolver, title, imageUri).apply {
            if (shareableText.isNotBlank()) {
                addItem(ClipData.Item(shareableText))
            }
        }
    } else {
        ClipData.newPlainText(title, shareableText)
    }
}