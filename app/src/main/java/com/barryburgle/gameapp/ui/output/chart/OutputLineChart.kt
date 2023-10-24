package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun SetLineChart(barEntryList: List<BarEntry>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val barChart = LineChart(context)
            val dataset =
                LineDataSet(barEntryList, "Label").apply { color = primaryColor.toArgb() }
            val barData = LineData(dataset)
            barChart.data = barData
            barChart.invalidate()
            barChart
        })
}