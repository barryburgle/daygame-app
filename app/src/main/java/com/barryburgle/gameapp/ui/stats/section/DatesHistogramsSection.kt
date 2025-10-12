package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.DatesHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp,
    onEvent: (StatsEvent) -> Unit
) {
    item {
        state.datesAgeHistogram.map { ageHistogram ->
            ageHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), ageHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                statsLoadInfo = StatsLoadInfoEnum.DATE_AGES,
                onEvent = onEvent
            )
        }
    }
    item {
        state.datesNumberHistogram.map { ageHistogram ->
            ageHistogram.metric?.let {
                BarEntry(
                    it.toFloat(), ageHistogram.frequency
                )
            }
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                statsLoadInfo = StatsLoadInfoEnum.DATE_NUMBER,
                onEvent = onEvent
            )
        }
    }
    item {
        state.datesNationalityHistogram.indices.map { index ->
            BarEntry(
                index.toFloat(), state.datesNationalityHistogram[index].frequency
            )
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                categories = state.datesNationalityHistogram.map { CountryEnum.getFlagByAlpha3(it.category) },
                statsLoadInfo = StatsLoadInfoEnum.DATE_COUNTRIES,
                onEvent = onEvent
            )
        }
    }
}