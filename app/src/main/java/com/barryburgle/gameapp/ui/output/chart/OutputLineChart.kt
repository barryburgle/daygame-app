package com.barryburgle.gameapp.ui.output.chart

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.core.content.ContextCompat
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.utilities.InsertInvite
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
    movingAverageWindow: Int
) {
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val commonLineWidth = 1f
    val inChartTextSize = 12f
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                Shapes.large
            )
    ) {
        val darkThemeEnabled = isSystemInDarkTheme()
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
                        Text(
                            text = description,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    Shapes.large
                                ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                InsertInvite(barEntryList, description, MaterialTheme.typography.titleMedium)
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        val barChart =
                            styleLineChart(
                                LineChart(context),
                                surfaceColor,
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
                                lineWidth = commonLineWidth
                                isHighlightEnabled = true
                                setDrawHighlightIndicators(false)
                                setDrawCircles(true)
                                circleRadius = 2f
                                circleColors = listOf(onSurfaceColor)
                                circleHoleColor = onSurfaceColor
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                setDrawFilled(true)
                                if (darkThemeEnabled) {
                                    fillDrawable =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.bg_output_line_b
                                        )
                                } else {
                                    fillDrawable =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.bg_output_line_w
                                        )
                                }
                            }
                        val averageDataset =
                            LineDataSet(
                                SessionManager.computeAverageBarEntryList(barEntryList),
                                "Average"
                            ).apply {
                                color = Color.YELLOW
                                lineWidth = commonLineWidth
                                setDrawValues(false)
                                setDrawCircles(false)
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                enableDashedLine(15f, 10f, 0f)
                            }
                        val movingAverageDataset =
                            LineDataSet(
                                SessionManager.computeMovingAverage(
                                    barEntryList,
                                    minOf(movingAverageWindow, barEntryList.size)
                                ),
                                "Last ${movingAverageWindow} average"
                            ).apply {
                                color = Color.RED
                                lineWidth = commonLineWidth
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
    }
}

fun styleLineChart(
    lineChart: LineChart,
    surfaceColor: Int,
    onSurfacecolor: Int,
    inChartTextSize: Float
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

