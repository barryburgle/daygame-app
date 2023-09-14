package main.manager

import main.model.Session

class SessionManager {
    companion object {
        fun computeIndexMovingAverage(sessions: Array<Session>, window: Int): Double {
            val reversedSessions: Array<Session> = sessions.reversedArray()
            var totalIndex = 0.0
            for (i in reversedSessions.indices) {
                if (i < window) {
                    totalIndex += reversedSessions.get(i).index
                }
            }
            return totalIndex / window.toDouble()
        }
    }
    // TODO: compute sets histogram
    // TODO: compute convos histogram
    // TODO: compute contacts histogram
    // TODO: week total sets
    // TODO: week total convos
    // TODO: week total contacts
    // TODO: week average index
    // TODO: week convo ratio
    // TODO: week contact ratio
    // TODO: week total time spent
    // TODO: week frequency
}