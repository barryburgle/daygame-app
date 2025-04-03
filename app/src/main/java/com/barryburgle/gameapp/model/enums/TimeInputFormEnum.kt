package com.barryburgle.gameapp.model.enums

enum class TimeInputFormEnum(private val field: String) : FieldEnum {
    DATE("date"),
    HOUR("hour");

    override fun getField(): String {
        return field
    }
}