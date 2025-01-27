package com.barryburgle.gameapp.ui.output.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import com.barryburgle.gameapp.ui.output.OutputCard
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.github.mikephil.charting.data.BarEntry

fun LazyListScope.SessionSection(
    state: OutputState,
    height: Dp,
    width: Dp
) {
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
                height = height,
                width = width,
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
                height = height,
                width = width,
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
                height = height,
                width = width,
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
                height = height,
                width = width,
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
                height = height,
                width = width,
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
                height = height,
                width = width,
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
                height = height,
                width = width,
                chartLabel = "Contact Ratio",
                barEntryList = it as List<BarEntry>,
                integerValues = false,
                ratio = true,
                movingAverageWindow = state.movingAverageWindow
            )
        }
    }
}