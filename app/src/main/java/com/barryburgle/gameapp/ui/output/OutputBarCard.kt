package com.barryburgle.gameapp.ui.output

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.output.chart.OutputBarChart
import com.github.mikephil.charting.data.BarEntry

@Composable
fun OutputBarCard(
    height: Dp,
    width: Dp,
    chartLabel: String,
    modifier: Modifier = Modifier
        .height(height)
        .width(width)
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        ),
    barEntryList: List<BarEntry>,
    integerValues: Boolean,
    ratio: Boolean,
    categories: List<String>? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            OutputBarChart(
                barEntryList = barEntryList,
                description = chartLabel,
                integerValues = integerValues,
                ratio = ratio,
                categories = categories
            )
        }
    }
    Spacer(modifier = Modifier.width(5.dp))
}