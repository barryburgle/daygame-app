package com.barryburgle.gameapp.model.enums

enum class ContactTypeEnum(private val field: String) : FieldEnum {
    NUMBER("number"),
    SOCIAL("social");

    override fun getField(): String {
        return field
    }
}