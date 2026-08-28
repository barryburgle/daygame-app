package com.barryburgle.gameapp.ui.output.chart

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

data class LabeledBarEntry(
    val entry: BarEntry,
    val label: String
)

@Composable
fun OutputLineChart(
    labeledEntries: List<LabeledBarEntry>,
    description: String = "",
    integerValues: Boolean,
    movingAverageWindow: Int = 4,
    movingAverageActive: Boolean = true,
    legendActive: Boolean = true,
    transparentBackgroundActive: Boolean = false,
    paddingOn: Boolean = true,
    isScrollable: Boolean = false,
    modifier: Modifier = Modifier,
    showLabels: Boolean = false
) {
    val normalizedBarEntryList = labeledEntries.mapIndexed { index, labeledEntry ->
        BarEntry((index).toFloat(), labeledEntry.entry.y)
    }
    val defaultSurfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val surfaceColor = if (transparentBackgroundActive) Color.TRANSPARENT else defaultSurfaceColor
    val composeBackgroundColor =
        if (transparentBackgroundActive) androidx.compose.ui.graphics.Color.Transparent else MaterialTheme.colorScheme.surface

    val onSurfaceColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val commonLineWidth = 1f
    val inChartValueTextSize = 12f
    val inChartLabelTextSize = 10f
    Column(
        modifier = modifier
            .background(
                composeBackgroundColor,
                Shapes.large
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = if (paddingOn) Modifier
                    .padding(5.dp)
                    .fillMaxSize() else Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (description != "") {
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
                            SmallTitleText(description)
                        }
                    }
                }
                val gradientColors = intArrayOf(
                    MaterialTheme.colorScheme.onSurface.toArgb(),
                    surfaceColor
                )
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        val barChart =
                            styleLineChart(
                                LineChart(context),
                                surfaceColor,
                                onSurfaceColor,
                                inChartValueTextSize,
                                legendActive,
                                isScrollable
                            )
                        if (showLabels) {
                            val xAxisLabels = labeledEntries.map { it.label }
                            if (xAxisLabels.isNotEmpty()) {
                                barChart.xAxis.apply {
                                    isEnabled = true
                                    valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                                    position = XAxis.XAxisPosition.BOTTOM
                                    textColor = onSurfaceColor
                                    textSize = inChartLabelTextSize
                                    setDrawGridLines(false)
                                    granularity = 1f
                                    isGranularityEnabled = true
                                    setLabelCount(25, false)
                                }
                            }
                        }
                        val formatter: ValueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        val leftAxis: YAxis = barChart.getAxisLeft()
                        leftAxis.setValueFormatter(formatter)
                        val dataset =
                            LineDataSet(normalizedBarEntryList, description).apply {
                                color = onSurfaceColor
                                valueTextColor = onSurfaceColor
                                valueTextSize = inChartValueTextSize
                                setDrawValues(true)
                                if (integerValues) {
                                    valueFormatter = formatter
                                }
                                lineWidth = commonLineWidth
                                isHighlightEnabled = true
                                setDrawHighlightIndicators(false)
                                setDrawCircles(true)
                                circleRadius = 2f
                                circleColors = listOf(onSurfaceColor)
                                circleHoleColor = onSurfaceColor
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                setDrawFilled(true)
                                fillDrawable =
                                    GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        gradientColors
                                    )
                            }
                        val averageDataset =
                            LineDataSet(
                                SessionManager.computeAverageBarEntryList(normalizedBarEntryList),
                                "Average"
                            ).apply {
                                color = Color.YELLOW
                                lineWidth = commonLineWidth
                                setDrawValues(false)
                                setDrawCircles(false)
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                enableDashedLine(15f, 10f, 0f)
                            }
                        val dataSetsList = mutableListOf<LineDataSet>(dataset, averageDataset)
                        if (movingAverageActive) {
                            val movingAverageDataset =
                                LineDataSet(
                                    SessionManager.computeMovingAverage(
                                        normalizedBarEntryList,
                                        minOf(movingAverageWindow, normalizedBarEntryList.size)
                                    ),
                                    "Last ${movingAverageWindow} average"
                                ).apply {
                                    color = Color.RED
                                    lineWidth = commonLineWidth
                                    setDrawValues(false)
                                    setDrawCircles(false)
                                    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                }
                            dataSetsList.add(movingAverageDataset)
                        }
                        val barData = LineData(dataSetsList.toList())
                        barChart.data = barData
                        if (isScrollable) {
                            barChart.setVisibleXRangeMaximum(12f)
                            barChart.moveViewToX(normalizedBarEntryList.size.toFloat())
                        }
                        barChart.invalidate()
                        barChart
                    })
            }
        }
    }
}

fun styleLineChart(
    lineChart: LineChart,
    surfaceColor: Int,
    onSurfacecolor: Int,
    inChartTextSize: Float,
    legendActive: Boolean,
    isScrollable: Boolean = false
): LineChart {
    lineChart.apply {
        setBackgroundColor(surfaceColor)
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = false
        }
        xAxis.apply {
            isEnabled = false
        }
        setTouchEnabled(isScrollable)
        isDragEnabled = isScrollable
        isScaleXEnabled = isScrollable
        isScaleYEnabled = false
        setPinchZoom(false)
        description = null
        legend.isEnabled = legendActive
        legend.textColor = onSurfacecolor
        legend.textSize = inChartTextSize
        extraBottomOffset = 15f
    }
    return lineChart
}

