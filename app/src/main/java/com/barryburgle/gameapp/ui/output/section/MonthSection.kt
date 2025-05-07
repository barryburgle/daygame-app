package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.OutputCard
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
                BarEntry(
                    it.toFloat(),
                    singleMonth.sets
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.convos
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.contacts
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthDates.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.dates
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Dates",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.avgIndex
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Session Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.timeSpent
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Session Time [Hours]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthDates.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.dateTimeSpent
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Date Time [Hours]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.avgConvoRatio * 100
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Conv. Ratio [%]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    // TODO: consider adding to average ratios precise ratios for the period and std of ratios in the period (do same for weeks)
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleMonth.avgContactRatio * 100
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Contact Ratio [%]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        aggregatedMonthSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    GlobalStatsService.computeGenericRatio(
                        singleMonth.sets.toInt(),
                        aggregatedMonthDates.get(it).dates.toInt()
                    ).toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Date Ratio [%]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
}