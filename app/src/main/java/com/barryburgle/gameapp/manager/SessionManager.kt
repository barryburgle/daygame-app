package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.session.AbstractSession

class SessionManager {
    companion object {
        fun computeIndexMovingAverage(
            abstractSessions: Array<AbstractSession>,
            window: Int
        ): Double {
            val reversedAbstractSessions: Array<AbstractSession> = abstractSessions.reversedArray()
            var totalIndex = 0.0
            for (i in reversedAbstractSessions.indices) {
                if (i < window) {
                    totalIndex += reversedAbstractSessions.get(i).index
                }
            }
            return totalIndex / window.toDouble()
        }

        fun computeSetsHistogram(abstractSessions: Array<AbstractSession>): Map<Int, Double> {
            val histogramMap = mutableMapOf<Int, Double>()
            for (session in abstractSessions) {
                val count = session.sets
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, abstractSessions.size.toDouble());
        }

        fun computeConvosHistogram(abstractSessions: Array<AbstractSession>): Map<Int, Double> {
            val histogramMap = mutableMapOf<Int, Double>()
            for (session in abstractSessions) {
                val count = session.convos
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, abstractSessions.size.toDouble());
        }

        fun computeContactsHistogram(abstractSessions: Array<AbstractSession>): Map<Int, Double> {
            val histogramMap = mutableMapOf<Int, Double>()
            for (session in abstractSessions) {
                val count = session.contacts
                histogramMap[count] = histogramMap.getOrDefault(count, 0.0) + 1.0
            }
            return computePercentageFrequency(histogramMap, abstractSessions.size.toDouble());
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