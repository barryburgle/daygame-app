package com.barryburgle.gameapp.model.enums

enum class ChartType(private val field: String) : FieldEnum {
    SESSION("Sessions"),
    WEEK("Weeks"),
    MONTH("Months"),
    HISTOGRAM("Histograms");

    override fun getField(): String {
        return field
    }
}