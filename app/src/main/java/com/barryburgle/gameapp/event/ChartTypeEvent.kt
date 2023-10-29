package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.enums.ChartType

sealed interface ChartTypeEvent {
    data class SortCharts(val chartType: ChartType) : ChartTypeEvent
}