package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.chart.LabeledBarEntry
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.WeekSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
    val aggregatedWeekPeriodsList: List<AggregatedPeriod> =
        SessionManager.createAggregatedPeriodList(
            state.sessionsByWeek,
            state.datesByWeek
        )
    val aggregatedWeekSessions =
        SessionManager.getAggregatedSessions(aggregatedWeekPeriodsList)
    val aggregatedWeekDates =
        SessionManager.getAggregatedDates(aggregatedWeekPeriodsList)
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.sets
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Sets",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.convos
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Conversations",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.contacts
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Contacts",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekDates.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.dates
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Dates",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.avgIndex
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Session Index",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = false,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.timeSpent
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Session Time [Hours]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekDates.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.dateTimeSpent
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Date Time [Hours]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.avgConvoRatio * 100
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Conv. Ratio [%]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleWeek.avgContactRatio * 100
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Contact Ratio [%]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        GlobalStatsService.computeGenericRatio(
                            singleWeek.sets.toInt(),
                            aggregatedWeekDates.get(it).dates.toInt()
                        ).toFloat()
                    ),
                    singleWeek.label
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Date Ratio [%]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastWeeksShown
            )
        }
    }
}