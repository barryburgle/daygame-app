package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry


fun LazyListScope.MonthSection(state: OutputState) {
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
                chartLabel = "Monthly Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Monthly Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Monthly Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Monthly Average Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false
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
                chartLabel = "Monthly Average Conversation Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true
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
                chartLabel = "Monthly Average Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true
            )
        }
    }
}