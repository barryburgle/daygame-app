package com.barryburgle.gameapp.ui.output.state

import com.barryburgle.gameapp.model.enums.ChartType

data class OutputState(
    val chartType: ChartType = ChartType.ABSOLUTE
)