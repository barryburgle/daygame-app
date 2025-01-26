package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ChartTypeEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.model.enums.SelectionType
import com.barryburgle.gameapp.ui.output.section.HistogramSection
import com.barryburgle.gameapp.ui.output.section.MonthSection
import com.barryburgle.gameapp.ui.output.section.SessionSection
import com.barryburgle.gameapp.ui.output.section.WeekSection
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.utilities.SelectionRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutputScreen(
    state: OutputState,
    onEvent: (ChartTypeEvent) -> Unit
) {
    val spaceFromBottom = 60.dp // TODO: centralize across screens
    // TODO: make cards with injectable type of charts
    // TODO: make different types of charts injectable with arrays
    // TODO: substitute the following with table fetch
    Scaffold { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChartType.values().forEach { chartType ->
                            SelectionRow(
                                SelectionType.CHARTS,
                                state.chartType,
                                chartType,
                                onEvent as (GenericEvent) -> Unit
                            )
                        }
                    }
                }
            }
            if (state.abstractSessions.isNotEmpty()) {
                // TODO: refactor all the following labdas inside items to be called passing the abstract session attribute + description
                if (ChartType.SESSION.equals(state.chartType)) {
                    SessionSection(state)
                }
                if (ChartType.WEEK.equals(state.chartType)) {
                    WeekSection(state)
                }
                if (ChartType.MONTH.equals(state.chartType)) {
                    MonthSection(state)
                }
                if (ChartType.HISTOGRAM.equals(state.chartType)) {
                    HistogramSection(state)
                }
                item { Row(modifier = Modifier.height(spaceFromBottom)) {} }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Insert a new session")
                        }
                    }
                }
            }
        }
    }
}
