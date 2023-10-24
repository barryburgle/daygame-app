package com.barryburgle.gameapp.ui.output

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.output.chart.OutputLineChart
import com.github.mikephil.charting.data.BarEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputCard(
    chartLabel: String,
    modifier: Modifier = Modifier
        .height(250.dp)
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        ),
    barEntryList: List<BarEntry>
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            OutputLineChart(barEntryList = barEntryList, description = chartLabel)
        }
    }
}