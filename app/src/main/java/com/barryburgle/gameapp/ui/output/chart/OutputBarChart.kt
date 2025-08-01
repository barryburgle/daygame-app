package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun OutputBarChart(
    barEntryList: List<BarEntry>,
    description: String,
    integerValues: Boolean,
    ratio: Boolean,
    categories: List<String>? = null
) {
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val inChartTextSize = 12f
    Column(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.surface, Shapes.large
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
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
                AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                    val barChart = styleBarChart(
                        BarChart(context),
                        surfaceColor,
                        barEntryList,
                        ratio,
                        categories
                    )
                    val formatter: ValueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }
                    val leftAxis: YAxis = barChart.getAxisLeft()
                    leftAxis.setValueFormatter(formatter)
                    val dataset =
                        BarDataSet(barEntryList, description).apply {
                            color = onSurfaceColor
                            valueTextColor = onSurfaceColor
                            valueTextSize = inChartTextSize
                            setDrawValues(true)
                            if (integerValues) {
                                valueFormatter = IntegerValueFormatter()
                            }
                            isHighlightEnabled = true
                            setGradientColor(surfaceColor, onSurfaceColor)
                            styleXAxis(barChart, onSurfaceColor, categories)
                        }
                    val barData = BarData(dataset)
                    barChart.data = barData
                    barChart.setFitBars(true);
                    barChart.animateY(300, Easing.EaseInOutQuad);
                    barChart.invalidate()
                    barChart
                })
            }
        }
    }
}

fun styleBarChart(
    barChart: BarChart,
    surfaceColor: Int,
    barEntryList: List<BarEntry>,
    ratio: Boolean,
    categories: List<String>? = null
): BarChart {
    barChart.apply {
        setBackgroundColor(surfaceColor)
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = false
            if (!ratio) {
                axisMinimum = -1f
                val max = barEntryList.maxOf { barEntry -> barEntry.y }.toInt()
                axisMaximum = max.toFloat() + 1
            } else {
                axisMinimum = -.05f
                axisMaximum = 1.05f
            }
        }
        xAxis.apply {
            isEnabled = true
            setDrawGridLines(false)
        }
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
        extraBottomOffset = 35f
    }
    return barChart
}

fun styleXAxis(
    barChart: BarChart, textColor: Int, categories: List<String>? = null
) {
    // TODO: specify on x axis label as Sets/converstaions/contacts
    // TODO: specify on y axis label as "Sessions" (vertically written)
    val xAxisFormatter = IntegerValueFormatter()
    xAxisFormatter.label = ""
    val xAxis: XAxis = barChart.getXAxis()
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    if (categories != null) {
        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
    } else {
        xAxis.valueFormatter = xAxisFormatter
    }
    xAxis.textSize = 15f
    xAxis.textColor = textColor
    xAxis.setDrawGridLines(false)
    xAxis.setDrawAxisLine(false)
    xAxis.yOffset = 2f
    xAxis.granularity = 1f
}