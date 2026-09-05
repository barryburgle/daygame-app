package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.recording.RecordingState
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.recording.RecordingService
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.AbstractSessionService
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.dialog.SessionCounters
import com.barryburgle.gameapp.ui.utilities.RecordingsView
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.timeline.Timeline

@Composable
fun LiveSessionBody(
    sessionTime: Long,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit,
    onEvent: (GameEvent) -> Unit,
    abstractSession: AbstractSession,
    liveSessionLeads: Int,
    liveSessionShareEnabled: Boolean,
    copyReportOnClipboard: Boolean,
    recordingState: RecordingState = RecordingState(),
    recordings: List<String> = emptyList(),
    recordingsFolder: String = "",
    recordingsEnabled: Boolean = false,
    pinPoints: List<PinPoint>,
    leads: List<Lead>,
    pullOClockReminderInterval: Int
) {
    var setsCount = abstractSession.sets + liveSessionLeads
    var convosCount = abstractSession.convos + liveSessionLeads
    var contactsCount = abstractSession.contacts + liveSessionLeads
    LittleBodyText("Live session:")
    Spacer(modifier = Modifier.height(7.dp))
    SessionCounters(
        setsCount = setsCount,
        convosCount = convosCount,
        contactsCount = contactsCount,
        onSetsChange = { newVal ->
            onEvent(GameEvent.SetSetsLive(abstractSession, newVal))
        },
        onConvosChange = { newVal, isIncreasing ->
            onEvent(GameEvent.SetConvosLive(abstractSession, newVal, isIncreasing))
        },
        onContactsChange = { newVal, isIncreasing ->
            onEvent(GameEvent.SetContactsLive(abstractSession, newVal, isIncreasing))
        },
        liveSessionShareEnabled = liveSessionShareEnabled,
        copyReportOnClipboard = copyReportOnClipboard,
        onEvent = onEvent,
        pullOClockReminderInterval = pullOClockReminderInterval
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DescribedQuantifier(
            quantity = FormatService.getPerc(
                AbstractSessionService.computeConvoRatio(
                    convosCount,
                    setsCount
                )
            ),
            quantityFontSize = perfFontSize,
            description = "Conversation\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = FormatService.getPerc(
                AbstractSessionService.computeContactRatio(
                    contactsCount,
                    setsCount
                )
            ),
            quantityFontSize = perfFontSize,
            description = "Contact\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${
                AbstractSessionService.computeIndex(
                    setsCount,
                    convosCount,
                    contactsCount,
                    sessionTime
                )
            }",
            quantityFontSize = perfFontSize,
            description = "Index",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${
                AbstractSessionService.computeApproachTime(
                    sessionTime,
                    setsCount
                )
            }",
            quantityFontSize = perfFontSize,
            description = "Minutes\nper set",
            descriptionFontSize = descriptionFontSize
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    RecordingsView(
        recordingState = recordingState,
        recordings = RecordingService.recordingsOf(abstractSession.id, recordings),
        recordingsFolder = recordingsFolder,
        recordingsEnabled = recordingsEnabled,
        onTapRecordingStart = { onEvent(GameEvent.TapRecordingStart(abstractSession.id!!)) },
        onTapRecordingStop = { onEvent(GameEvent.TapRecordingStop) },
        onTapRecordingDiscard = { onEvent(GameEvent.TapRecordingDiscard(it)) },
        onTapPlaybackPlay = { onEvent(GameEvent.TapPlaybackPlay(it)) },
        onTapPlaybackPause = { onEvent(GameEvent.TapPlaybackPause) },
        onTapRecordingDelete = { onEvent(GameEvent.TapRecordingDelete(it)) },
        onSetPlaybackPosition = { onEvent(GameEvent.SetPlaybackPosition(it)) }
    )

    if (pinPoints.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Timeline(
            abstractSession,
            pinPoints,
            sessionTime,
            leads,
            onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}