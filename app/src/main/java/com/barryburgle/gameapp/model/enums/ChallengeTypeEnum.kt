package com.barryburgle.gameapp.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.ui.graphics.vector.ImageVector

enum class ChallengeTypeEnum(
    private val type: String,
    private val description: String,
    private val successVerb: String,
    private val icon: ImageVector
) {
    SET("set", "Sets", "done", Icons.Default.DirectionsRun),
    CONVERSATION("conversation", "Conversations", "had", Icons.Default.RecordVoiceOver),
    CONTACT("contact", "Contacts", "taken", Icons.Default.ContactPhone);
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

    fun getIcon(): ImageVector {
        return icon
    }

    companion object {
        fun isTypeAchievedInteger(type: String): Boolean {
            return when (type) {
                SET.getType() -> true
                CONTACT.getType() -> true
                CONVERSATION.getType() -> true
                else -> false
            }
        }

        fun getIcon(type: String): ImageVector {
            return when (type) {
                SET.getType() -> SET.icon
                CONTACT.getType() -> CONTACT.icon
                CONVERSATION.getType() -> CONVERSATION.icon
                else -> Icons.Default.EmojiEvents
            }
        }

        fun getDescription(type: String): String {
            return when (type) {
                SET.getType() -> SET.getDescription()
                CONTACT.getType() -> CONTACT.getDescription()
                CONVERSATION.getType() -> CONVERSATION.getDescription()
                else -> "Type"
            }
        }

        fun getValue(type: String): ChallengeTypeEnum {
            return when (type) {
                SET.getType() -> SET
                CONTACT.getType() -> CONTACT
                CONVERSATION.getType() -> CONVERSATION
                else -> SET
            }
        }
    }
}