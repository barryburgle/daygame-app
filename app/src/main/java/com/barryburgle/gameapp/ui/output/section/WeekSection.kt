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
                chartLabel = "Weekly Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Weekly Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Weekly Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Weekly Average Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false
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
                chartLabel = "Weekly Average Conversation Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true
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
                chartLabel = "Weekly Average Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true
            )
        }
    }
}