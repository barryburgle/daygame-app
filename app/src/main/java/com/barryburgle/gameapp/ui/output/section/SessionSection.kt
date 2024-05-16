package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.SessionSection(state: OutputState) {
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.sets.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Sets",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.convos.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Conversations",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.contacts.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Contacts",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.index.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Index",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.approachTime.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Approach Time",
                barEntryList = it as List<BarEntry>,
                integerValues = true,
                ratio = false,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.convoRatio.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Conversation Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
    item {
        state.abstractSessions.map { abstractSession ->
            abstractSession.id?.toInt()?.let {
                BarEntry(
                    it.toFloat(),
                    abstractSession.contactRatio.toFloat()
                )
            }
        }?.let { it ->
            OutputCard(
                chartLabel = "Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
}