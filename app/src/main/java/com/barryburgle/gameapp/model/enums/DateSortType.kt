package com.barryburgle.gameapp.model.enums

enum class DateSortType(private val field: String) : FieldEnum {
    LEAD("Lead"),
    LOCATION("Location"),
    DATE("Date"),
    START_TIME("Start Time"),
    END_TIME("End Time"),
    DATE_TIME("Date Time"),
    COST("Cost"),
    DATE_NUMBER("Date Number"),

    /*TODO: FOR THE FOLLOWING 5 BOOLEAN FIELD VALUES SHOW ONLY EITHER DATES FILTERED BY FIELD TO TRUE OR FALSE - NO ORDERING BUT FILTERING: PUT LABEL IN THE SELECTOR ie "Pull" - "Pulled" - "Not pulled"*/
    PULL("Pull"),
    BOUNCE("Bounce"),
    KISS("Kiss"),
    LAY("Lay"),
    RECORDED("Recorded"),
    DAY_OF_WEEK("Day of Week");

    override fun getField(): String {
        return field
    }
}