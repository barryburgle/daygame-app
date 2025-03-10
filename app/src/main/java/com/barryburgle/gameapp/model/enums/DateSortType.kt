package com.barryburgle.gameapp.model.enums

enum class DateSortType(private val field: String) : FieldEnum {
    DATE("Date"),
    LEAD("Lead"),
    LOCATION("Location"),
    START_TIME("Start Time"),
    END_TIME("End Time"),
    DATE_TIME("Date Time"),
    COST("Cost"),
    DATE_NUMBER("Date Number"),
    PULL("Pulled"),
    NOT_PULL("Not Pulled"),
    BOUNCE("Not Bounced"),
    NOT_BOUNCE("Not Bounced"),
    KISS("Kissed"),
    NOT_KISS("Not Kissed"),
    LAY("Laid"),
    NOT_LAY("Lay"),
    RECORDED("Recorded"),
    NOT_RECORDED("Recorded"),
    DAY_OF_WEEK("Day of Week");

    override fun getField(): String {
        return field
    }
}