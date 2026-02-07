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
    private val icon: ImageVector
) {
    SET("set", "Sets", Icons.Default.DirectionsRun),
    CONVERSATION("conversation", "Conversations", Icons.Default.RecordVoiceOver),
    CONTACT("contact", "Contacts", Icons.Default.ContactPhone);
    // TODO: add here goals on percs and indexes later. For a future task, the challenge type will determine how the challenge completion is computed from raw data (sessions, dates, sets)

    fun getType(): String {
        return type
    }

    fun getDescription(): String {
        return description
    }

    fun getIcon(): ImageVector {
        return icon
    }

    companion object {
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
    }
}