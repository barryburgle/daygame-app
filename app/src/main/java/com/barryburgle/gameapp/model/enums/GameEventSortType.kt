package com.barryburgle.gameapp.model.enums

enum class GameEventSortType(private val field: String) : FieldEnum {
    DATE("Date"),
    DAY_OF_WEEK("Day of Week");

    override fun getField(): String {
        return field
    }
}