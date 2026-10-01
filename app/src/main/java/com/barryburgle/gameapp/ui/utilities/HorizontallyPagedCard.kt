package com.barryburgle.gameapp.ui.utilities

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.TextureView
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
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
    val hasMedia = !backgroundMediaLocalUrl.isNullOrBlank()
    val textColor = MaterialTheme.colorScheme.onPrimary
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .height(170.dp)
            .padding(8.dp)
            .shadow(elevation = 12.dp, shape = cardShape, clip = false)
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
            if (hasMedia) {
                // TODO: support ig link -> cached thumbnail in card background (saved in a cache folder)
                RenderMedia(backgroundMediaLocalUrl, cardShape, Modifier.matchParentSize())
            }
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
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                val shownDescription =
                                    if (description.length <= 100) description else description.take(
                                        100
                                    ) + "..."
                                LittleBodyText(
                                    text = "\"$shownDescription\"",
                                    color = textColor
                                )
                            }
                        }
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
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
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
private fun RenderMedia(
    backgroundMediaLocalUrl: String,
    cardShape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(cardShape)
    ) {
        if (isVideoUrl(backgroundMediaLocalUrl)) {
            CardVideoBackground(
                videoUrl = backgroundMediaLocalUrl,
                modifier = Modifier.matchParentSize()
            )
        } else {
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
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                )
        )
    }
}

@Composable
private fun CardVideoBackground(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember(context, videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 0f
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            TextureView(ctx).apply {
                exoPlayer.setVideoTextureView(this)
            }
        },
        modifier = modifier.clipToBounds()
    )
}

private fun isVideoUrl(url: String): Boolean {
    val videoExtensions = listOf(".mp4", ".mkv", ".webm", ".avi", ".mov", ".3gp", ".m4v")
    val lower = url.lowercase()
    return videoExtensions.any { lower.contains(it) } || lower.contains("video")
}

@Composable
fun PillShapedTranslucent(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
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