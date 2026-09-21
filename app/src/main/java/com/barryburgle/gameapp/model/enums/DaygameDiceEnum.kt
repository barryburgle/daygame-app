package com.barryburgle.gameapp.model.enums

enum class DaygameDiceEnum(val value: Int, val description: String) {
    FRONT_STOP(1, "Classic front stop"),
    SHOP(2, "Open in a shop"),
    STANDING_WAITING_GIRL(3, "Open a standing/waiting"),
    SEATED_GIRL(4, "Open a seated"),
    TWO_SET(5, "Open a 2-set"),
    HOT_YOU_SAW_SHOP_ASSISTANCE(6, "Open the hottest you see");

    companion object {
        fun random(): DaygameDiceEnum = values().random()
    }
}