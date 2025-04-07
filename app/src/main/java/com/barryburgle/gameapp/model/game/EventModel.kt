package com.barryburgle.gameapp.model.game

import androidx.compose.ui.graphics.vector.ImageVector

interface EventModel {

    fun getDate(): String?

    fun getTitle(): String

    fun getIcon(): ImageVector

    fun getDescription(): String

    fun getDeleteEvent() // TODO: define return type

    fun getEditEvent() // TODO: define return type

    fun getLeadIds(): List<Long>

    fun getStickingPoints(): String?
}