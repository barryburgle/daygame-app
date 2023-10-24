package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.ui.theme.Shapes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun OutputLineChart(barEntryList: List<BarEntry>, description: String) {
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
            fontSize = 24.sp,
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
                        onSurfaceColor,
                        15f
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
                        valueTextColor = Color.White.toArgb()
                        setDrawValues(false)
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
    textColor: Int,
    fontSize: Float
): LineChart {
    lineChart.apply {
        setBackgroundColor(surfaceColor)
        axisRight.isEnabled = false
        axisLeft.apply {
            axisMinimum = -1f
            val max = barEntryList.maxOf { barEntry -> barEntry.y }.toInt()
            axisMaximum = max.toFloat() + 1
            labelCount = if (max % 2 == 0) {
                (max + 2) / 2
            } else {
                (max + 1) / 2
            }
            setDrawGridLines(true)
            setDrawAxisLine(true)
            textSize = fontSize
            setTextColor(textColor)
        }
        xAxis.apply {
            setDrawGridLines(true)
            setDrawAxisLine(true)
            position = XAxis.XAxisPosition.BOTTOM
            textSize = fontSize
            isEnabled = false
            setTextColor(textColor)
        }
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
        extraBottomOffset = 15f
    }
    return lineChart
}