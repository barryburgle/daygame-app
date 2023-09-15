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

        fun computeSetsHistogram(sessions: Array<Session>): Map<Int, Double> {
            var histogramMap = mutableMapOf<Int, Double>()
            for (session in sessions) {
                val count = session.sets.size
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, sessions.size.toDouble());
        }

        fun computeConvosHistogram(sessions: Array<Session>): Map<Int, Double> {
            var histogramMap = mutableMapOf<Int, Double>()
            for (session in sessions) {
                val count = session.convos.size
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, sessions.size.toDouble());
        }

        fun computeContactsHistogram(sessions: Array<Session>): Map<Int, Double> {
            var histogramMap = mutableMapOf<Int, Double>()
            for (session in sessions) {
                val count = session.contacts.size
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, sessions.size.toDouble());
        }

        private fun computePercentageFrequency(
            histogramMap: MutableMap<Int, Double>,
            totalSessions: Double
        ): Map<Int, Double> {
            for ((count, absoluteFrequency) in histogramMap) {
                val percentageFrequency = absoluteFrequency / totalSessions * 100
                histogramMap[count] = percentageFrequency
            }
            return histogramMap;
        }

    }
    // TODO: week total sets
    // TODO: week total convos
    // TODO: week total contacts
    // TODO: week average index
    // TODO: week convo ratio
    // TODO: week contact ratio
    // TODO: week total time spent
    // TODO: week frequency
}