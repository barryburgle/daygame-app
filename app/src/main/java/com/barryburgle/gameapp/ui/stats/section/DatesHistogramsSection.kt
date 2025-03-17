package com.barryburgle.gameapp.ui.stats.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.ui.output.OutputBarCard
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.DatesHistogramsSection(
    state: StatsState,
    height: Dp,
    width: Dp
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
                chartLabel = "Ages",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Number",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false
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
                chartLabel = "Countries",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                categories = state.datesNationalityHistogram.map { CountryEnum.getFlagByAlpha3(it.category) }
            )
        }
    }
}