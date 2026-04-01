package com.barryburgle.gameapp.ui.output

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.output.chart.OutputBarChart
import com.barryburgle.gameapp.ui.output.chart.OutputPieChart
import com.barryburgle.gameapp.ui.tool.utils.Switch
import com.barryburgle.gameapp.ui.utilities.button.LittleIconButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry

@Composable
fun OutputBarCard(
    height: Dp,
    width: Dp,
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
    categories: List<String>? = null,
    statsLoadInfo: StatsLoadInfoEnum,
    onEvent: (StatsEvent) -> Unit
) {
    var isPieChart by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallTitleText(statsLoadInfo.getTracker())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LittleIconButton(
                        imageVector = Icons.Filled.Info,
                        onClick = { onEvent(StatsEvent.ShowInfo(statsLoadInfo)) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    LittleBodyText("Pie chart")
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        flag = isPieChart,
                        onCheckedChange = { isPieChart = !isPieChart }
                    )
                }
            }

            if (isPieChart) {
                val pieEntryList = barEntryList.map {
                    val label = if (categories != null && it.x.toInt() in categories.indices) {
                        categories[it.x.toInt()]
                    } else {
                        it.x.toInt().toString()
                    }
                    PieEntry(it.y, label)
                }
                OutputPieChart(
                    pieEntryList = pieEntryList,
                    description = statsLoadInfo.getTracker()
                )
            } else {
                OutputBarChart(
                    barEntryList = barEntryList,
                    description = statsLoadInfo.getTracker(),
                    integerValues = integerValues,
                    ratio = ratio,
                    categories = categories,
                    statsLoadInfo = statsLoadInfo,
                    onEvent = onEvent,
                    showTitleAndInfo = false
                )
            }
        }
    }
    Spacer(modifier = Modifier.width(5.dp))
}
