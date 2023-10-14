package com.barryburgle.gameapp.service

import com.barryburgle.gameapp.model.session.AbstractSession

interface SessionService {

    // TODO: evaluate if use this interface for services or not

    fun saveSession(
        abstractSession: AbstractSession
    )

    fun getSessions(): List<AbstractSession>
}