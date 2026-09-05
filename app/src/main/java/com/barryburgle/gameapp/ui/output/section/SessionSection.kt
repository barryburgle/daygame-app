package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.chart.LabeledBarEntry
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.SessionSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.sets.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Sets",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.convos.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Conversations",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.contacts.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Contacts",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.index.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Index",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = false,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.approachTime.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Approach Time [minutes]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.convoRatio.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Conversation Ratio [%]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
    item {
        state.allSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                LabeledBarEntry(
                    BarEntry(
                        it.toFloat(),
                        abstractSession.contactRatio.toFloat()
                    ),
                    FormatService.getDateForCharLabel(abstractSession.date)
                )
            }
        }?.let { it ->
            OutputCard(
                height = height,
                width = width,
                chartLabel = "Contact Ratio [%]",
                labeledEntries = it as List<LabeledBarEntry>,
                integerValues = true,
                movingAverageWindow = state.movingAverageWindow,
                lastShown = state.lastSessionsShown
            )
        }
    }
}