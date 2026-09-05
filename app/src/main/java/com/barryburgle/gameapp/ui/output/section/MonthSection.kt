package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.chart.LabeledBarEntry
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry


fun LazyListScope.MonthSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
    val aggregatedMonthPeriodsList: List<AggregatedPeriod> =
        SessionManager.createAggregatedPeriodList(
            state.sessionsByMonth,
            state.datesByMonth
        )
    val aggregatedMonthSessions = SessionManager.getAggregatedSessions(
        aggregatedMonthPeriodsList
    )
    val aggregatedMonthDates = SessionManager.getAggregatedDates(
        aggregatedMonthPeriodsList
    )
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.sets
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.convos
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.contacts
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthDates.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.dates
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.avgIndex
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.timeSpent
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthDates.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.dateTimeSpent
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.avgConvoRatio * 100
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    // TODO: consider adding to average ratios precise ratios for the period and std of ratios in the period (do same for weeks) [v1.7.3]
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        singleMonth.avgContactRatio * 100
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        GlobalStatsService.computeGenericRatio(
                            singleMonth.sets.toInt(),
                            aggregatedMonthDates.get(it).dates.toInt()
                        ).toFloat()
                    ),
                    singleMonth.label
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
                lastShown = state.lastMonthsShown
            )
        }
    }
}