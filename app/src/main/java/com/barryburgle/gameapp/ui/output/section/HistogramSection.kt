package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.HistogramSection(state: OutputState) {
    item {
        state.setsHistogram.map { setsHistogram ->
            setsHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), setsHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                chartLabel = "Sets Histogram",
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
                chartLabel = "Convos Histogram",
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
                chartLabel = "Contacts Histogram",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
            )
        }
    }
}