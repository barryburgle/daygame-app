package com.barryburgle.gameapp.ui.output.chart

import com.github.mikephil.charting.formatter.ValueFormatter

class IntegerValueFormatter : ValueFormatter() {
    var label: String = ""

    override fun getFormattedValue(value: Float): String {
        val integer = value.toInt()
        return integer.toString() + label
    }
}