package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.OutputCard
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
        SessionManager.getAggregatedSessions(aggregatedWeekPeriodsList, state.lastWeeksShown)
    val aggregatedWeekDates =
        SessionManager.getAggregatedDates(aggregatedWeekPeriodsList, state.lastWeeksShown)
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.sets
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
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.convos
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
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.contacts
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
        aggregatedWeekDates.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.dates
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
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.avgIndex
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
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.timeSpent
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
        aggregatedWeekDates.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.dateTimeSpent
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
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.avgConvoRatio * 100
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
    item {
        aggregatedWeekSessions.map { singleWeek ->
            singleWeek.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    singleWeek.avgContactRatio * 100
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
        aggregatedWeekSessions.map { singleMonth ->
            singleMonth.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    GlobalStatsService.computeGenericRatio(
                        singleMonth.sets.toInt(),
                        aggregatedWeekDates.get(it).dates.toInt()
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