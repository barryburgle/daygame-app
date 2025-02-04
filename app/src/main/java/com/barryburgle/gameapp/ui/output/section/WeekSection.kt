package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.WeekSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
    item {
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.sets
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
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.convos
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
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.contacts
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
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.avgIndex
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
        state.weekStats.map { weekStat ->
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
                chartLabel = "Time Spent [Hours]",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.avgConvoRatio * 100
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
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.avgContactRatio * 100
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