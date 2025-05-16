package com.barryburgle.gameapp.manager

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.stat.AggregatedDates
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.model.stat.AggregatedSessions
import com.github.mikephil.charting.data.BarEntry

class SessionManager {
    companion object {

        // TODO: do unit tests for getAggregatedSessions [v1.7.3]
        fun getAggregatedSessions(
            aggregatedPeriodList: List<AggregatedPeriod>
        ): List<AggregatedSessions> {
            var aggregatedSessions: List<AggregatedSessions> = mutableListOf()
            var count: Int = 1
            for (aggregatedPeriod in aggregatedPeriodList) {
                var addingAggregatedSessions = AggregatedSessions(0f, 0f, 0f, 0f, 0f, 0f, count, 0f)
                if (aggregatedPeriod.aggregatedSessions != null) {
                    addingAggregatedSessions = aggregatedPeriod.aggregatedSessions!!
                    addingAggregatedSessions.periodNumber = count
                }
                aggregatedSessions += addingAggregatedSessions
                count++
            }
            for (index in aggregatedSessions.indices) {
                aggregatedSessions[index].periodNumber = index
            }
            return aggregatedSessions
        }

        // TODO: do unit tests for getAggregatedDates [v1.7.3]
        fun getAggregatedDates(
            aggregatedPeriodList: List<AggregatedPeriod>
        ): List<AggregatedDates> {
            var aggregatedDates: List<AggregatedDates> = mutableListOf()
            var count: Int = 1
            for (aggregatedPeriod in aggregatedPeriodList) {
                var addingAggregatedDates = AggregatedDates(0f, count, 0f)
                if (aggregatedPeriod.aggregatedDates != null) {
                    addingAggregatedDates = aggregatedPeriod.aggregatedDates!!
                    addingAggregatedDates.periodNumber = count
                }
                aggregatedDates += addingAggregatedDates
                count++
            }
            for (index in aggregatedDates.indices) {
                aggregatedDates[index].periodNumber = index
            }
            return aggregatedDates
        }

        fun createAggregatedPeriodList(
            aggregatedSessionsList: List<AggregatedSessions>,
            aggregatedDatesList: List<AggregatedDates>
        ): List<AggregatedPeriod> {
            var aggregatedPeriodMap: Map<Int, AggregatedPeriod> = mutableMapOf()
            for (aggregatedSessions in aggregatedSessionsList) {
                var aggregatedPeriod: AggregatedPeriod? =
                    aggregatedPeriodMap.get(aggregatedSessions.periodNumber)
                if (aggregatedPeriod != null) {
                    aggregatedPeriod.aggregatedSessions = aggregatedSessions
                } else {
                    aggregatedPeriod = AggregatedPeriod(aggregatedSessions, null)
                }
                aggregatedPeriodMap += aggregatedSessions.periodNumber to aggregatedPeriod
            }
            for (aggregatedDates in aggregatedDatesList) {
                var aggregatedPeriod: AggregatedPeriod? =
                    aggregatedPeriodMap.get(aggregatedDates.periodNumber)
                if (aggregatedPeriod != null) {
                    aggregatedPeriod.aggregatedDates = aggregatedDates
                } else {
                    aggregatedPeriod = AggregatedPeriod(null, aggregatedDates)
                }
                aggregatedPeriodMap += aggregatedDates.periodNumber to aggregatedPeriod
            }
            aggregatedPeriodMap = aggregatedPeriodMap.toSortedMap()
            return aggregatedPeriodMap.entries.toList().map { it.value }
        }

        fun normalizeSessionsIds(
            abstractSessions: List<AbstractSession>
        ): List<AbstractSession> {
            for (index in abstractSessions.indices) {
                abstractSessions[index].id = index.toLong()
            }
            return abstractSessions
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
            barEntryList: List<BarEntry>, movingAverageWindow: Int
        ): List<BarEntry> {
            val window = if (movingAverageWindow > 0) movingAverageWindow else 1
            var valuesList = ArrayList<Float>()
            barEntryList.map { barEntry -> valuesList.add(barEntry.y) }
            var averageList = ArrayList<Float>()
            for (windowSize in 0 until window - 1) {
                val lastAverage = valuesList.windowed(windowSize + 1, 1) { it.average() }.get(0)
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
                        (count++).toFloat(), average
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
            histogramMap: MutableMap<Int, Double>, totalSessions: Double
        ): Map<Int, Double> {
            for ((count, absoluteFrequency) in histogramMap) {
                val percentageFrequency = absoluteFrequency / totalSessions * 100
                histogramMap[count] = percentageFrequency
            }
            return histogramMap;
        }
    }
}