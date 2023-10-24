package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.ui.theme.Shapes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun OutputLineChart(
    barEntryList: List<BarEntry>,
    description: String,
    integerValues: Boolean,
    ratio: Boolean
) {
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                Shapes.large
            )
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    Shapes.large
                )
                .padding(10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                val barChart =
                    styleLineChart(
                        LineChart(context),
                        surfaceColor,
                        barEntryList,
                        ratio
                    )
                val formatter: ValueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
                val leftAxis: YAxis = barChart.getAxisLeft()
                leftAxis.setValueFormatter(formatter)
                val dataset =
                    LineDataSet(barEntryList, "Label").apply {
                        color = onSurfaceColor
                        valueTextColor = onSurfaceColor
                        valueTextSize = 12f
                        setDrawValues(true)
                        if (integerValues) {
                            valueFormatter = IntegerValueFormatter()
                        }
                        lineWidth = 3f
                        isHighlightEnabled = true
                        setDrawHighlightIndicators(false)
                        setDrawCircles(true)
                        circleRadius = 4f
                        circleColors = listOf(onSurfaceColor)
                        circleHoleColor = onSurfaceColor
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    }
                val barData = LineData(dataset)
                barChart.data = barData
                barChart.invalidate()
                barChart
            })
    }
}

fun styleLineChart(
    lineChart: LineChart,
    surfaceColor: Int,
    barEntryList: List<BarEntry>,
    ratio: Boolean
): LineChart {
    lineChart.apply {
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
            isEnabled = false
        }
        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
        extraBottomOffset = 15f
    }
    return lineChart
}

private class IntegerValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val integer = value.toInt()
        return integer.toString()
    }
}