package com.barryburgle.gameapp.model.enums

enum class HeatmapEntityEnum(private val field: String, private val isInt: Boolean) : FieldEnum {
    SETS("Sets", true),
    CONVERSATIONS("Conversations", true),
    CONTACTS("Contacts", true),
    INDEX("Index", false),
    DATES("Dates", true),
    RECORDINGS("Recordings", true),
    PULLED("Pulled", true),
    BOUNCED("Bounced", true),
    KISSED("Kissed", true),
    LAID("Laid", true);

    override fun getField(): String {
        return field
    }

    fun isInt(): Boolean {
        return isInt
    }
}