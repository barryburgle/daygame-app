package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.SessionsHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp,
    onEvent: (StatsEvent) -> Unit
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
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                statsLoadInfo = StatsLoadInfoEnum.SESSION_SETS,
                onEvent = onEvent
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
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                statsLoadInfo = StatsLoadInfoEnum.SESSION_CONVOS,
                onEvent = onEvent
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
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                statsLoadInfo = StatsLoadInfoEnum.SESSION_CONTACTS,
                onEvent = onEvent
            )
        }
    }
}