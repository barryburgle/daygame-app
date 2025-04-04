package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.PeriodAware
import com.github.mikephil.charting.data.BarEntry

class SessionManager {
    companion object {
        fun normalizeSessionsIds(
            abstractSessions: List<AbstractSession>
        ): List<AbstractSession> {
            for (index in abstractSessions.indices) {
                abstractSessions[index].id = index.toLong()
            }
            return abstractSessions
        }

        fun normalizeIds(
            periodAwareList: List<PeriodAware>
        ): List<PeriodAware> {
            for (index in periodAwareList.indices) {
                periodAwareList[index].periodNumber = index
            }
            return periodAwareList
        }

        fun computeAverageBarEntryList(barEntryList: List<BarEntry>): List<BarEntry> {
            var total = 0.0f
            for (barEntry in barEntryList) {
                total += barEntry.y
            }
            val avg = total / barEntryList.size
            val avgBarEntryList = ArrayList<BarEntry>()
            for (barEntry in barEntryList) {
                avgBarEntryList.add(BarEntry(barEntry.x, avg))
            }
            return avgBarEntryList
        }

        fun computeMovingAverage(
            barEntryList: List<BarEntry>,
            movingAverageWindow: Int
        ): List<BarEntry> {
            val window = if (movingAverageWindow > 0) movingAverageWindow else 1
            var valuesList = ArrayList<Float>()
            barEntryList.map { barEntry -> valuesList.add(barEntry.y) }
            var averageList = ArrayList<Float>()
            for (windowSize in 0 until window - 1) {
                val lastAverage =
                    valuesList.windowed(windowSize + 1, 1) { it.average() }.get(0)
                averageList.add(lastAverage.toFloat())
            }
            val remainderAverage =
                valuesList.windowed(window, 1) { it.average() }.map { item -> item.toFloat() }
            var avgBarEntryList = ArrayList<BarEntry>()
            var count = 0
            averageList.addAll(remainderAverage)
            averageList.map { average ->
                avgBarEntryList.add(
                    BarEntry(
                        (count++).toFloat(),
                        average
                    )
                )
            }
            return avgBarEntryList.toList()
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
}