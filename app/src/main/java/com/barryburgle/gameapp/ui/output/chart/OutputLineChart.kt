package com.barryburgle.gameapp.ui.output.chart

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.core.content.ContextCompat
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.manager.SessionManager
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
    val inChartTextSize = 12f
    val movingAverageWindow = 4
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                Shapes.large
            )
    ) {
        val darkThemeEnabled = isSystemInDarkTheme()
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
                        ratio,
                        onSurfaceColor,
                        inChartTextSize
                    )
                val formatter: ValueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
                val leftAxis: YAxis = barChart.getAxisLeft()
                leftAxis.setValueFormatter(formatter)
                val dataset =
                    LineDataSet(barEntryList, description).apply {
                        color = onSurfaceColor
                        valueTextColor = onSurfaceColor
                        valueTextSize = inChartTextSize
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
                        setDrawFilled(true)
                        if (darkThemeEnabled) {
                            fillDrawable =
                                ContextCompat.getDrawable(context, R.drawable.bg_output_line_b)
                        } else {
                            fillDrawable =
                                ContextCompat.getDrawable(context, R.drawable.bg_output_line_w)
                        }
                    }
                val averageDataset =
                    LineDataSet(
                        SessionManager.computeAverageBarEntryList(barEntryList),
                        "Average"
                    ).apply {
                        color = Color.YELLOW
                        lineWidth = 1f
                        setDrawValues(false)
                        setDrawCircles(false)
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                        enableDashedLine(15f, 10f, 0f)
                    }
                val movingAverageDataset =
                    LineDataSet(
                        SessionManager.computeMovingAverage(barEntryList, movingAverageWindow),
                        "Last ${movingAverageWindow} average"
                    ).apply {
                        color = Color.RED
                        lineWidth = 2f
                        setDrawValues(false)
                        setDrawCircles(false)
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    }
                val barData = LineData(dataset, averageDataset, movingAverageDataset)
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
    ratio: Boolean,
    onSurfacecolor: Int,
    inChartTextSize: Float
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
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = true
        legend.textColor = onSurfacecolor
        legend.textSize = inChartTextSize
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