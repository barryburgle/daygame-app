package com.barryburgle.gameapp.model.enums

enum class ChallengeSortType(private val field: String) : FieldEnum {
    START_DATE("Start Date"),
    END_DATE("End Date"),
    TYPE("Type"),
    TYPE_AND_GOAL("Type and Goal");

    override fun getField(): String {
        return field
    }
}

// TODO: divide enums by domain/model-class and under that for function by creating packages