package com.barryburgle.gameapp.ui.stats.dialog

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.stat.CategoryHistogram
import com.barryburgle.gameapp.model.stat.Histogram
import com.barryburgle.gameapp.service.bitmap.BitmapService
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.utilities.animation.AnimatedStaggeredItem
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import kotlinx.coroutines.launch

@Composable
fun InfoDialog(
    state: StatsState,
    onEvent: (StatsEvent) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val localContext = context.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    val perfFontSize = 50.sp
    val descriptionFontSize = 10.sp

    if (state.completeHistogram.isNotEmpty()) {
        val descriptionFrequencyPairs = getHistogramDataPoints(state.completeHistogram)

        FlowDialog(
            onDismissRequest = { onEvent(StatsEvent.HideInfo) },
            onConfirm = { onEvent(StatsEvent.HideInfo) },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.65f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LargeTitleText(state.infoDialogTitle, true)
                        LittleBodyText(state.trackedEntity + " grouped by " + state.infoDialogTitle.lowercase())
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconShadowButton(
                            onClick = {
                                coroutineScope.launch {
                                    val histogramData = exportHistogramDataPoints(
                                        state.infoDialogTitle,
                                        state.trackedEntity,
                                        descriptionFrequencyPairs
                                    )
                                    // TODO: export also title and desc from dialog
                                    val bitmap = try {
                                        graphicsLayer.toImageBitmap().asAndroidBitmap()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        null
                                    }

                                    val imageUri = bitmap?.let {
                                        BitmapService.saveBitmapToCache(
                                            context, it
                                        )
                                    }

                                    if (state.copyReportOnClipboard) {
                                        if (imageUri != null) {
                                            val systemClipboard =
                                                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            val clipData = ClipData.newUri(
                                                context.contentResolver,
                                                "Histogram Report",
                                                imageUri
                                            ).apply {
                                                addItem(ClipData.Item(histogramData))
                                            }
                                            systemClipboard.setPrimaryClip(clipData)
                                        } else {
                                            clipboardManager.setText(
                                                AnnotatedString(histogramData)
                                            )
                                        }
                                        Toast.makeText(
                                            localContext,
                                            "Histogram & card copied",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, histogramData)
                                        if (imageUri != null) {
                                            putExtra(Intent.EXTRA_STREAM, imageUri)
                                            type = "image/png"
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        } else {
                                            type = "text/plain"
                                        }
                                    }
                                    val shareIntent = Intent.createChooser(
                                        sendIntent,
                                        "Share histogram"
                                    )
                                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    localContext.startActivity(shareIntent)
                                }
                            },
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Histogram"
                        )
                    }
                }
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier.drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawContent()
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(descriptionFrequencyPairs.size) { index ->
                        val pair = descriptionFrequencyPairs[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedStaggeredItem(index = 0) {
                                DescribedQuantifier(
                                    quantity = pair.first,
                                    quantityFontSize = perfFontSize,
                                    description = state.infoDialogTitle,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                            AnimatedStaggeredItem(index = 1) {
                                DescribedQuantifier(
                                    quantity = pair.second,
                                    quantityFontSize = perfFontSize,
                                    description = state.trackedEntity,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun exportHistogramDataPoints(
    tracker: String, trackedEntity: String, descriptionFrequencyPairs: List<Pair<String, String>>
): String {
    var export = tracker + "," + trackedEntity + "\n"
    for (pair in descriptionFrequencyPairs) {
        export += pair.first + ", " + pair.second + "\n"
    }
    return export
}

fun getHistogramDataPoints(completeHistogram: List<Any>): List<Pair<String, String>> {
    var descriptionFrequencyPairs: List<Pair<String, String>> = emptyList()
    val maxCountryNameLength = 13
    for (histogramDataPoint in completeHistogram) {
        var description = ""
        var frequency = ""
        when (histogramDataPoint) {
            is CategoryHistogram -> {
                val tempHistogramDataPoint = histogramDataPoint
                var countryName =
                    CountryEnum.getCountryNameByAlpha3(tempHistogramDataPoint.category)
                var extreme = countryName.length
                var suffix = ""
                if (countryName.length > maxCountryNameLength) {
                    extreme = maxCountryNameLength
                    suffix = "..."
                }
                countryName = countryName.substring(0, extreme) + suffix
                description =
                    CountryEnum.getFlagByAlpha3(tempHistogramDataPoint.category) + " " + countryName
                frequency = tempHistogramDataPoint.frequency.toInt().toString()
            }

            is Histogram -> {
                val tempHistogramDataPoint = histogramDataPoint
                description = tempHistogramDataPoint.metric.toInt().toString()
                frequency = tempHistogramDataPoint.frequency.toInt().toString()
            }
        }
        val newPair = description to frequency
        descriptionFrequencyPairs = descriptionFrequencyPairs + newPair
    }
    return descriptionFrequencyPairs
}