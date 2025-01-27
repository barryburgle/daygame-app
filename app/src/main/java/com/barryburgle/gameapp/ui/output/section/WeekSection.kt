package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.WeekSection(state: OutputState) {
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
                chartLabel = "Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
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
                chartLabel = "Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
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
                chartLabel = "Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
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
                chartLabel = "Average Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.avgConvoRatio
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Average Conv. Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.weekStats.map { weekStat ->
            weekStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    weekStat.avgContactRatio
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Average Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
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
                chartLabel = "Time Spent [Hours]",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
}