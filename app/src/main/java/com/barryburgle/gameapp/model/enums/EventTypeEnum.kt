package com.barryburgle.gameapp.model.enums

enum class EventTypeEnum(private val field: String) : FieldEnum {
    SESSION("Session"),
    SET("Set"),
    DATE("Date"),
    ALL("All");

    override fun getField(): String {
        return field
    }

    companion object {
        fun getFieldsButAll(): List<String> {
            return values().filter { it != ALL }.map { it.getField() + "s" }
        }
    }
}