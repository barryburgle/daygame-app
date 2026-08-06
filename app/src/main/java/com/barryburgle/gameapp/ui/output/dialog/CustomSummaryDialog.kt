package com.barryburgle.gameapp.ui.output.dialog

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
import androidx.compose.runtime.remember
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
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.enums.HeatmapEntityEnum
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.output.getSeries
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import java.time.LocalDate

data class SummaryEntryModel(
    val label: String,
    val total: Float,
    val average: Float,
    val isIndex: Boolean
)

@Composable
fun CustomSummaryDialog(
    customSummaryStartDate: LocalDate,
    customSummaryEndDate: LocalDate,
    state: OutputState,
    onEvent: (OutputEvent) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    val semiOpaqueBackground = MaterialTheme.colorScheme.surfaceVariant
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp

    // Optimized: Memoize filtering and aggregation so it doesn't recalculate unnecessarily on recomposition
    val summaryEntries = remember(customSummaryStartDate, customSummaryEndDate, state) {
        listOf(
            HeatmapEntityEnum.SETS to getSeries(state, HeatmapEntityEnum.SETS),
            HeatmapEntityEnum.CONVERSATIONS to getSeries(state, HeatmapEntityEnum.CONVERSATIONS),
            HeatmapEntityEnum.CONTACTS to getSeries(state, HeatmapEntityEnum.CONTACTS),
            HeatmapEntityEnum.INDEX to getSeries(state, HeatmapEntityEnum.INDEX),
            HeatmapEntityEnum.DATES to getSeries(state, HeatmapEntityEnum.DATES),
            HeatmapEntityEnum.RECORDINGS to getSeries(state, HeatmapEntityEnum.RECORDINGS),
            HeatmapEntityEnum.PULLED to getSeries(state, HeatmapEntityEnum.PULLED),
            HeatmapEntityEnum.BOUNCED to getSeries(state, HeatmapEntityEnum.BOUNCED),
            HeatmapEntityEnum.KISSED to getSeries(state, HeatmapEntityEnum.KISSED),
            HeatmapEntityEnum.LAID to getSeries(state, HeatmapEntityEnum.LAID)
        ).map { (entity, series) ->
            val filteredSeries = series.filter { entry ->
                !entry.date.isBefore(customSummaryStartDate) && !entry.date.isAfter(customSummaryEndDate)
            }

            val isIndex = entity == HeatmapEntityEnum.INDEX
            val total = if (isIndex) 0f else filteredSeries.sumOf { it.count.toDouble() }.toFloat()
            val average = if (filteredSeries.isNotEmpty()) {
                filteredSeries.map { it.count }.average().toFloat()
            } else {
                0f
            }

            SummaryEntryModel(
                label = entity.getField(),
                total = total,
                average = average,
                isIndex = isIndex
            )
        }
    }

    val reportText = buildString {
        append("Custom Summary Report (${FormatService.getDate(customSummaryStartDate.toString() + "T00:00Z")} to ${FormatService.getDate(customSummaryEndDate.toString() + "T00:00Z")})\n")
        summaryEntries.forEach { entry ->
            if (entry.isIndex) {
                append("${entry.label} - Avg: ${entry.average}\n")
            } else {
                append("${entry.label} - Total: ${entry.total.toInt()}, Avg: ${entry.average}\n")
            }
        }
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(OutputEvent.SwitchShowCustomSummaryDialog)
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
                        LargeTitleText("Custom Summary")
                        LittleBodyText(
                            "${FormatService.getDate(customSummaryStartDate.toString() + "T00:00Z")} to ${
                                FormatService.getDate(
                                    customSummaryEndDate.toString() + "T00:00Z"
                                )
                            }"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(5.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconShadowButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(reportText))
                                Toast.makeText(
                                    localContext,
                                    "Summary report copied",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, reportText)
                                    type = "text/plain"
                                }
                                val shareIntent =
                                    Intent.createChooser(sendIntent, "Share summary report")
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                localContext.startActivity(shareIntent)
                            },
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Summary"
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
                ) { _ ->
                    LazyColumn(
                        modifier = Modifier
                            .height(300.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        items(summaryEntries.size) { index ->
                            val entry = summaryEntries[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (entry.isIndex) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DescribedQuantifier(
                                            quantity = entry.average.toString(),
                                            quantityFontSize = perfFontSize,
                                            description = entry.label,
                                            descriptionFontSize = descriptionFontSize
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        DescribedQuantifier(
                                            quantity = entry.total.toInt().toString(),
                                            quantityFontSize = perfFontSize,
                                            description = "${entry.label} (Total)",
                                            descriptionFontSize = descriptionFontSize
                                        )
                                        DescribedQuantifier(
                                            quantity = entry.average.toString(),
                                            quantityFontSize = perfFontSize,
                                            description = "${entry.label} (Avg)",
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
                onEvent(OutputEvent.SwitchShowCustomSummaryDialog)
            }
        }
    )
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