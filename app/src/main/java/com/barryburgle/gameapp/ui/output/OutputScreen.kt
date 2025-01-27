package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ChartTypeEvent
import com.barryburgle.gameapp.ui.output.section.HistogramSection
import com.barryburgle.gameapp.ui.output.section.MonthSection
import com.barryburgle.gameapp.ui.output.section.SessionSection
import com.barryburgle.gameapp.ui.output.section.WeekSection
import com.barryburgle.gameapp.ui.output.state.OutputState

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
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Leads:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column {

                    }
                }
            }
            val heigh: Dp = 200.dp
            val width: Dp = 320.dp
            if (state.abstractSessions.isNotEmpty()) {
                item {
                    Text(
                        text = "Sessions:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyRow {
                        SessionSection(state, heigh, width)
                    }
                }
                item {
                    Text(
                        text = "Weeks:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyRow {
                        WeekSection(state, heigh, width)
                    }
                }
                item {
                    Text(
                        text = "Months:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyRow {
                        MonthSection(state, heigh, width)
                    }
                }
                item {
                    Text(
                        text = "Histograms:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyRow {
                        HistogramSection(state, heigh, width)
                    }
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
