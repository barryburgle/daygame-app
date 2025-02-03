package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.SessionsHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp
) {
    item {
        state.setsHistogram.map { setsHistogram ->
            setsHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), setsHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                chartLabel = "Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
            )
        }
    }
    item {
        state.convosHistogram.map { convosHistogram ->
            convosHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), convosHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                chartLabel = "Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
            )
        }
    }
    item {
        state.contactsHistogram.map { contactsHistogram ->
            contactsHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), contactsHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                chartLabel = "Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
            )
        }
    }
}