package com.barryburgle.gameapp.ui.stats.dialog

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun InfoDialog(
    state: StatsState,
    onEvent: (StatsEvent) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    val semiOpaqueBackground = MaterialTheme.colorScheme.surfaceVariant
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp
    if (state.completeHistogram.isNotEmpty()) {
        val descriptionFrequencyPairs = getHistogramDataPoints(state.completeHistogram)
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.shadow(elevation = 10.dp),
            onDismissRequest = {
                onEvent(StatsEvent.HideInfo)
            },
            title = {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(0.65f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LargeTitleText(state.infoDialogTitle)
                            LittleBodyText(state.trackedEntity + " grouped by " + state.infoDialogTitle.lowercase())
                        }
                        Column(
                            modifier = Modifier
                                .padding(5.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconShadowButton(
                                onClick = {
                                    var histogramData = exportHistogramDataPoints(
                                        state.infoDialogTitle,
                                        state.trackedEntity,
                                        descriptionFrequencyPairs
                                    )
                                    if (state.copyReportOnClipboard) {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                histogramData
                                            )
                                        )
                                        Toast.makeText(
                                            localContext,
                                            "Histogram copied",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            histogramData
                                        )
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(
                                        sendIntent,
                                        "Share histogram"
                                    )
                                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    localContext.startActivity(shareIntent)
                                },
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Histogram"
                            )
                        }
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Scaffold(
                        topBar = {
                            Row(
                                modifier = getBlurBarModifier(
                                    listOf(
                                        semiOpaqueBackground,
                                        semiOpaqueBackground.copy(0.5f),
                                        semiOpaqueBackground.copy(0.01f),
                                    )
                                )
                            ) {}
                        },
                        bottomBar = {
                            Row(
                                modifier = getBlurBarModifier(
                                    listOf(
                                        semiOpaqueBackground.copy(0.01f),
                                        semiOpaqueBackground.copy(0.5f),
                                        semiOpaqueBackground,
                                    )
                                )
                            ) {}
                        },
                        modifier = Modifier
                            .height(300.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .fillMaxWidth()
                    ) { padding ->
                        LazyColumn(
                            modifier = Modifier
                                .height(300.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .fillMaxWidth()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            for (pair in descriptionFrequencyPairs) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.7f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            DescribedQuantifier(
                                                quantity = pair.first,
                                                quantityFontSize = perfFontSize,
                                                description = state.infoDialogTitle,
                                                descriptionFontSize = descriptionFontSize
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
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
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                ConfirmButton {
                    onEvent(StatsEvent.HideInfo)
                }
            })
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
                var tempHistogramDataPoint = histogramDataPoint as CategoryHistogram
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
                var tempHistogramDataPoint = histogramDataPoint as Histogram
                description = tempHistogramDataPoint.metric.toInt().toString()
                frequency = tempHistogramDataPoint.frequency.toInt().toString()
            }
        }
        val newPair = description to frequency
        descriptionFrequencyPairs = descriptionFrequencyPairs + newPair
    }
    return descriptionFrequencyPairs
}

fun getBlurBarModifier(colorList: List<Color>): Modifier {
    return Modifier
        .fillMaxWidth()
        .height(10.dp)
        .background(
            brush = Brush.verticalGradient(
                colors = colorList,
            )
        )
}