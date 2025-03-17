package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry


fun LazyListScope.MonthSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
    item {
        state.sessionsByMonth.map { singleMonth ->
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
        state.sessionsByMonth.map { singleMonth ->
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
        state.sessionsByMonth.map { singleMonth ->
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
        state.datesByMonth.map { singleMonth ->
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
        state.sessionsByMonth.map { singleMonth ->
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
                chartLabel = "Average Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.sessionsByMonth.map { weekStat ->
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
        state.datesByMonth.map { weekStat ->
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
        state.sessionsByMonth.map { singleMonth ->
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
    item {
        state.sessionsByMonth.map { singleMonth ->
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
}