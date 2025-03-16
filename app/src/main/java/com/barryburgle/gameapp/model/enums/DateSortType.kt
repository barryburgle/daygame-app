package com.barryburgle.gameapp.model.enums

enum class DateSortType(private val field: String) : FieldEnum {
    DATE("Date"),
    LEAD("Lead"),
    LOCATION("Location"),
    DATE_TIME("Date Time"),
    COST("Cost"),
    DATE_NUMBER("Date Number"),
    DATE_TYPE("Date Type"),
    PULL("Pulled"),
    NOT_PULL("Not Pulled"),
    BOUNCE("Bounced"),
    NOT_BOUNCE("Not Bounced"),
    KISS("Kissed"),
    NOT_KISS("Not Kissed"),
    LAY("Laid"),
    NOT_LAY("Not Laid"),
    RECORD("Recorded"),
    NOT_RECORD("Not Recorded"),
    DAY_OF_WEEK("Day of Week"),
    WEEK_NUMBER("Week Number");

    override fun getField(): String {
        return field
    }
}