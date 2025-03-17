package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.LeadsHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp
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
                chartLabel = "Ages",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Countries",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                categories = state.leadsNationalityHistogram.map { CountryEnum.getFlagByAlpha3(it.category) }
            )
        }
    }
}