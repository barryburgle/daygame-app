package com.barryburgle.gameapp.model.enums

enum class SetSortType(private val field: String) : FieldEnum {
    DATE("Date"),
    START_HOUR("Hour"),
    SET_TIME("Duration"),
    LOCATION("Location"),
    LEAD("Lead"),
    CONVERSATION("Talk"),
    NO_CONVERSATION("No Talk"),
    CONTACT("Contact"),
    NO_CONTACT("No Contact"),
    INSTANT_DATE("Instant Date"),
    NO_INSTANT_DATE("No Instant Date"),
    RECORDED("Recorded"),
    NOT_RECORDED("Not Recorded"),
    DAY_OF_WEEK("Day of Week"),
    WEEK_NUMBER("Week Number");

    override fun getField(): String {
        return field
    }
}