package com.barryburgle.gameapp.model.enums

enum class EventTypeEnum(private val field: String) : FieldEnum {
    SESSION("Session"),
    SET("Set"),
    DATE("Date");

    override fun getField(): String {
        return field
    }

    companion object {
        fun getAllFields(): List<String> {
            return values().map { it.getField() }
        }
    }
}