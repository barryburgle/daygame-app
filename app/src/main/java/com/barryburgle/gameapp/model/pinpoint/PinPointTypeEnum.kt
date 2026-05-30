package com.barryburgle.gameapp.model.pinpoint

import com.barryburgle.gameapp.model.enums.FieldEnum

enum class PinPointTypeEnum(private val field: String) : FieldEnum {
    /**
     * The values of this enum have to bew considered as inclusive of the sub values:
     * - a set is just a set
     * - a conversation is a conversation and a set
     * - a contact is a contact, a conversation and a set
     * This allows less code on the saving, inputting just one row for event
     * and queries that use the definitions above to get pinpoint events by type
     */
    SET("set"),
    CONVERSATION("conversation"),
    CONTACT("contact");

    override fun getField(): String {
        return field
    }
}
