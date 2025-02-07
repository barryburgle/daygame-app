package com.barryburgle.gameapp.service.batch

import com.barryburgle.gameapp.model.session.AbstractSession

interface BatchSessionInitializer {
    fun init(
        id: String?,
        date: String,
        startHour: String,
        endHour: String,
        sets: String,
        convos: String,
        contacts: String,
        stickingPoints: String
    ): AbstractSession
}