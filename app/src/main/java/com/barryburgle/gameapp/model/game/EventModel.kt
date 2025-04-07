package com.barryburgle.gameapp.model.game

import androidx.compose.ui.graphics.vector.ImageVector

interface EventModel {

    fun getEventDate(): String?

    fun getEventTitle(): String

    fun getEventIcon(): ImageVector

    fun getEventDescription(): String

    fun getEventDeleteMethod() // TODO: define return type

    fun getEventEditMethod() // TODO: define return type

    fun getEventLeadIds(): List<Long>

    fun getEventStickingPoints(): String?
}