package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputScreen(
    state: OutputState
) {
    val spaceFromTop = 5.dp // TODO: centralize across screens
    val spaceFromBottom = 60.dp // TODO: centralize across screens
    // TODO: make cards with injectable type of charts
    // TODO: make different types of charts injectable with arrays
    // TODO: substitute the following with table fetch
    Scaffold {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.abstractSessions.isNotEmpty()) {
                // TODO: refactor all the following labdas inside items to be called passing the abstract session attribute + description
                item {
                    Row(modifier = Modifier.height(spaceFromTop)) {}
                }
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
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
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
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
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
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
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
                            chartLabel = "Index",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = false
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
                            chartLabel = "Approach Time",
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
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
                            chartLabel = "Conversation Ratio",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = true
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
                            chartLabel = "Contact Ratio",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = true
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.sets
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Sets",
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.convos
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Conversations",
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.contacts
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Contacts",
                            barEntryList = it as List<BarEntry>,
                            integerValues = true,
                            ratio = false
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.avgIndex
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Average Index",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = false
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.avgConvoRatio
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Average Conversation Ratio",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = true
                        )
                    }
                }
                item {
                    state.weekStats.map { weekStat ->
                        weekStat.weekNumber?.let {
                            BarEntry(
                                it.toFloat(),
                                weekStat.avgContactRatio
                            )
                        }
                    }?.let { it ->
                        OutputCard(
                            chartLabel = "Weekly Average Contact Ratio",
                            barEntryList = it as List<BarEntry>,
                            integerValues = false,
                            ratio = true
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