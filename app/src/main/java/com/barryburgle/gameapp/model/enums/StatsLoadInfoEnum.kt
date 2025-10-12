package com.barryburgle.gameapp.model.enums

enum class StatsLoadInfoEnum(
    private val tracker: String,
    private val trackedEntity: String
) {
    SESSION_SETS("Sets", "Sessions"),
    SESSION_CONVOS(
        "Conversations",
        "Sessions"
    ),
    SESSION_CONTACTS(
        "Contacts",
        "Sessions"
    ),
    LEAD_AGES(
        "Age",
        "Leads"
    ),
    LEAD_COUNTRIES(
        "Country",
        "Leads"
    ),
    DATE_AGES(
        "Age",
        "Dates"
    ),
    DATE_NUMBER(
        "Date number",
        "Dates"
    ),
    DATE_COUNTRIES(
        "Country",
        "Dates"
    );

    fun getTracker(): String {
        return tracker
    }

    fun getTrackedEntity(): String {
        return trackedEntity
    }
}