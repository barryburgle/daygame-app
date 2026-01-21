package com.barryburgle.gameapp.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.ui.graphics.vector.ImageVector

enum class EventTypeEnum(private val field: String, private val icon: ImageVector?) : FieldEnum {
    SESSION("Session", Icons.Default.GroupAdd),
    SET("Set", Icons.Default.PersonAddAlt1),
    DATE("Date", Icons.Default.Favorite),
    CHALLENGE("Challenge", Icons.Default.EmojiEvents),
    ALL("All", null);

    override fun getField(): String {
        return field
    }

    fun getIcon(): ImageVector? {
        return icon
    }

    companion object {
        fun getFieldsButAll(): List<String> {
            return values().filter { it != ALL }.map { it.getField() + "s" }
        }
    }
}