package com.barryburgle.gameapp.model.enums

enum class SortType(private val field: String) : FieldEnum {
    DATE("Date"),
    SETS("Sets"),
    CONVOS("Conversations"),
    CONTACS("Contacts"),
    SESSION_TIME("Session Time"),
    APPROACH_TIME("Approach Time"),
    CONVO_RATIO("Conversation Ratio"),
    REJECTION_RATIO("Rejection Ratio"),
    CONTACT_RATIO("Contact Ratio"),
    INDEX("Index"),
    DAY_OF_WEEK("Day of Week"),
    WEEK_NUMBER("Week Number");

    override fun getField(): String {
        return field
    }
}