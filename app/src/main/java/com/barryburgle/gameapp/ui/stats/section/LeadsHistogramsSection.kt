package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.LeadsHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp,
    onEvent: (StatsEvent) -> Unit
) {
    item {
        state.leadsAgeHistogram.map { ageHistogram ->
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
                statsLoadInfo = StatsLoadInfoEnum.LEAD_AGES,
                onEvent = onEvent
            )
        }
    }
    item {
        state.leadsNationalityHistogram.indices.map { index ->
            BarEntry(
                index.toFloat(), state.leadsNationalityHistogram[index].frequency
            )
        }?.let { it ->
            OutputBarCard(
                height = height,
                width = width,
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                categories = state.leadsNationalityHistogram.map { CountryEnum.getFlagByAlpha3(it.category) },
                statsLoadInfo = StatsLoadInfoEnum.LEAD_COUNTRIES,
                onEvent = onEvent
            )
        }
    }
}