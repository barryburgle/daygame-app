package com.barryburgle.gameapp.model.enums

import androidx.annotation.DrawableRes
import com.barryburgle.gameapp.R

enum class ChallengeTypeEnum(
    private val type: String,
    private val description: String,
    private val successVerb: String,
    @DrawableRes private val icon: Int
) {
    SET("set", "Sets", "done", R.drawable.set_action), CONVERSATION(
        "conversation",
        "Conversations",
        "had",
        R.drawable.conversation_action
    ),
    CONTACT("contact", "Contacts", "taken", R.drawable.contact_action), DATE(
        "date",
        "Dates",
        "had",
        R.drawable.favorite
    );
    // TODO: add here goals on percs and indexes later. For a future task, the challenge type will determine how the challenge completion is computed from raw data (sessions, dates, sets)

    fun getType(): String {
        return type
    }

    fun getDescription(): String {
        return description
    }

    fun getSuccessVerb(): String {
        return successVerb
    }

    fun getIcon(): Int {
        return icon
    }

    companion object {
        fun isTypeAchievedInteger(type: String): Boolean {
            return when (type) {
                SET.getType() -> true
                CONTACT.getType() -> true
                CONVERSATION.getType() -> true
                DATE.getType() -> true
                else -> false
            }
        }

        fun getIcon(type: String): Int {
            return when (type) {
                SET.getType() -> SET.icon
                CONTACT.getType() -> CONTACT.icon
                CONVERSATION.getType() -> CONVERSATION.icon
                DATE.getType() -> DATE.icon
                else -> 0
            }
        }

        fun getDescription(type: String): String {
            return when (type) {
                SET.getType() -> SET.getDescription()
                CONTACT.getType() -> CONTACT.getDescription()
                CONVERSATION.getType() -> CONVERSATION.getDescription()
                DATE.getType() -> DATE.getDescription()
                else -> "Type"
            }
        }

        fun getValue(type: String): ChallengeTypeEnum {
            return when (type) {
                SET.getType() -> SET
                CONTACT.getType() -> CONTACT
                CONVERSATION.getType() -> CONVERSATION
                DATE.getType() -> DATE
                else -> SET
            }
        }
    }
}