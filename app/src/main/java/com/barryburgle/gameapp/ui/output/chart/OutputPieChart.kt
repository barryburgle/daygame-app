package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.Locale

@Composable
fun OutputPieChart(
    pieEntryList: List<PieEntry>,
    description: String
) {
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    var selectedIndex by remember { mutableStateOf(-1) }
    val total = pieEntryList.sumOf { it.y.toDouble() }.toFloat()
    val sortedEntries = pieEntryList.sortedByDescending { it.y }
    val processedEntries = mutableListOf<PieEntry>()
    if (sortedEntries.size <= 4) {
        processedEntries.addAll(sortedEntries)
    } else {
        processedEntries.addAll(sortedEntries.take(4))
        val othersValue = sortedEntries.drop(4).sumOf { it.y.toDouble() }.toFloat()
        processedEntries.add(PieEntry(othersValue, "Others"))
    }
    val shades = listOf(
        surfaceVariant.copy(alpha = 1f),
        surfaceVariant.copy(alpha = 0.7f),
        onSurfaceVariant.copy(alpha = 0.5f),
        onSurfaceVariant.copy(alpha = 0.8f),
        onSurfaceVariant.copy(alpha = 1f)
    ).map { it.toArgb() }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(180.dp)
                .padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            processedEntries.forEachIndexed { index, entry ->
                val baseColor = Color(shades[index % shades.size])
                val isHighlighted = selectedIndex == index

                val percentage = String.format(Locale.ROOT, "%.1f", (entry.y / total) * 100)

                val labelText = if (entry.label == "Others") {
                    "Others"
                } else if (entry.label.length <= 3) {
                    val flag = CountryEnum.getFlagByAlpha3(entry.label)
                    val name = CountryEnum.getCountryNameByAlpha3(entry.label)
                    if (name.isNotEmpty()) "$flag $name" else entry.label
                } else {
                    entry.label
                }

                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(baseColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        LittleBodyText(
                            text = "$labelText: $percentage%",
                            color = if (isHighlighted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PieChart(context).apply {
                    this.description.isEnabled = false
                    this.setUsePercentValues(true)
                    this.isDrawHoleEnabled = false
                    this.legend.isEnabled = false
                    this.setDrawEntryLabels(false)
                    this.setTouchEnabled(true)
                    this.setRotationEnabled(false)
                    this.setExtraOffsets(0f, 0f, 0f, 0f)

                    this.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (h != null) {
                                selectedIndex = h.x.toInt()
                            }
                        }

                        override fun onNothingSelected() {
                            selectedIndex = -1
                        }
                    })
                }
            },
            update = { pieChart ->
                val dataSet = PieDataSet(processedEntries, description).apply {
                    colors = shades
                    setDrawValues(false)
                    sliceSpace = 2f
                    selectionShift = 8f
                }
                pieChart.data = PieData(dataSet)
                pieChart.invalidate()
            }
        )
    }
}
