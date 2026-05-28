package com.barryburgle.gameapp.model.pinpoint

import com.barryburgle.gameapp.model.enums.FieldEnum

enum class PinPointTypeEnum(private val field: String) : FieldEnum {
    SET("set"),
    CONVERSATION("conversation"),
    CONTACT("contact"),
    LEAD("lead");

    override fun getField(): String {
        return field
    }
}
