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
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.sets
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.convos
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.contacts
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.avgIndex
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.avgConvoRatio
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Conv. Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { monthStat ->
            monthStat.periodNumber?.let {
                BarEntry(
                    it.toFloat(),
                    monthStat.avgContactRatio
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Average Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.monthStats.map { weekStat ->
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
                integerValues = false,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
}