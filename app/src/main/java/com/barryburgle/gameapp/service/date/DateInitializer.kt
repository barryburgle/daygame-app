package com.barryburgle.gameapp.service.date

import com.barryburgle.gameapp.model.date.Date

interface DateInitializer {
    fun init(
        id: String?,
        leadId: String?,
        location: String,
        date: String,
        startHour: String,
        endHour: String,
        cost: Int?,
        dateNumber: Int,
        dateType: String,
        pull: Boolean,
        bounce: Boolean,
        kiss: Boolean,
        lay: Boolean,
        recorded: Boolean,
        stickingPoints: String,
        tweetUrl: String
    ): Date
}