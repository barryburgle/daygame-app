package com.barryburgle.gameapp.service.set

import com.barryburgle.gameapp.model.set.SingleSet

interface SetInitializer {
    fun init(
        id: String?,
        date: String,
        startHour: String,
        endHour: String,
        sessionId: Long?,
        location: String,
        conversation: Boolean,
        contact: Boolean,
        instantDate: Boolean,
        recorded: Boolean,
        leadId: Long?,
        dateId: Long?,
        stickingPoints: String,
        tweetUrl: String
    ): SingleSet
}