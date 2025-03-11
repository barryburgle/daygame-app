package com.barryburgle.gameapp.model.enums

enum class DateType(private val type: String) {
    DRINK("drink"),
    COFFEE("coffee"),
    WALK("walk"),
    FOOD("food"),
    EXPERIENCE("experience");

    fun getType(): String {
        return type
    }
}