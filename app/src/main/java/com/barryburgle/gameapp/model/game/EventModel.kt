package com.barryburgle.gameapp.model.game

import androidx.compose.ui.graphics.vector.ImageVector

interface EventModel {

    fun getEventDate(): String?

    fun getEventTitle(): String

    fun getEventIcon(): ImageVector

    fun getEventDescription(): String

    fun getEventStickingPoints(): String?
}