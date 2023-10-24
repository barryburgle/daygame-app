package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.ChartType
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputScreen(
    state: OutputState, onEvent: (AbstractSessionEvent) -> Unit
) {
    val spaceFromTop = 80.dp // TODO: centralize across screens
    val spaceFromBottom = 60.dp // TODO: centralize across screens
    // TODO: make cards with injectable type of charts
    // TODO: make different types of charts injectable with arrays
    // TODO: substitute the following with table fetch
    Scaffold {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 10.dp)
                    ) {
                        ChartType.values().forEach { chartType ->
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        // TODO: show different charts
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.chartType == chartType, onClick = {
                                        // TODO: show different charts
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary,
                                        unselectedColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                                Text(
                                    text = chartType.field,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.abstractSessions.isNotEmpty()) {
                // TODO: refactor all the following labdas inside items to be called passing the abstract session attribute + description
                item {
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.sets.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Sets",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.convos.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Conversations",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.contacts.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Contacts",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    // TODO: use chart with double on y axis
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.index.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Index [to move]",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.approachTime.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Approach Time [to move]",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    // TODO: use chart with double on y axis
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.convoRatio.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Conversation Ratio [to move]",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item {
                    // TODO: use chart with double on y axis
                    state.abstractSessions.map { abstractSession ->
                        abstractSession.id?.toInt()?.let {
                            BarEntry(
                                it.toFloat(),
                                abstractSession.contactRatio.toFloat()
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Contact Ratio [to move]",
                            barEntryList = it as List<BarEntry>
                        )
                    }
                }
                item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
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

