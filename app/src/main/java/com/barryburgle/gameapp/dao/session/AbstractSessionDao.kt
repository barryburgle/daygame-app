package com.barryburgle.gameapp.dao.session

import com.barryburgle.gameapp.model.session.AbstractSession

class AbstractSessionDao {
    companion object {
        val abstractSessionList = mutableListOf<AbstractSession>()

        fun insertSession(abstractSession: AbstractSession) {
            // TODO: substitute with real db insert
            abstractSessionList.add(abstractSession)
        }

        fun selectLastSession(): AbstractSession {
            // TODO: substitute with real db select
            return abstractSessionList.last()
        }
    }
}