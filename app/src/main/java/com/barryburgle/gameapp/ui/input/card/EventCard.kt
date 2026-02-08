package com.barryburgle.gameapp.ui.input.card

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.model.enums.EventTypeEnum
import com.barryburgle.gameapp.model.game.SortableGameEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.card.body.ChallengeBody
import com.barryburgle.gameapp.ui.input.card.body.DateBody
import com.barryburgle.gameapp.ui.input.card.body.SessionBody
import com.barryburgle.gameapp.ui.input.card.body.SetBody
import com.barryburgle.gameapp.ui.input.dialog.leadName
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.body.MediumBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import java.time.temporal.ChronoUnit

@ExperimentalMaterial3Api
@Composable
fun EventCard(
    sortableGameEvent: SortableGameEvent,
    leads: List<Lead>,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
    simplePlusOneReport: Boolean,
    neverShareLeadInfo: Boolean,
    copyReportOnClipboard: Boolean
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    LittleBodyText(sortableGameEvent.event.getEventDescription())
                    Spacer(modifier = Modifier.width(3.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = sortableGameEvent.event.getEventIcon(),
                                contentDescription = "Session date",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            LargeTitleText(sortableGameEvent.event.getEventTitle())
                        }
                        Row(
                            modifier = Modifier
                                .width(160.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconShadowButton(
                                onClick = {
                                    var leadsToShare = leads
                                    if (neverShareLeadInfo) {
                                        leadsToShare = listOf()
                                    }
                                    var report = sortableGameEvent.event.shareReport(leadsToShare)
                                    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        var eventDate: Date = sortableGameEvent.event as Date
                                        report = eventDate.shareDateReport(
                                            leadsToShare,
                                            simplePlusOneReport
                                        )
                                    } else if (AchievedChallenge::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        var achievedChallenge: AchievedChallenge =
                                            sortableGameEvent.event as AchievedChallenge
                                        var completePercentage =
                                            achievedChallenge.achieved / achievedChallenge.challenge.goal
                                        var achievedToPrint = achievedChallenge.achieved.toString()
                                        if (ChallengeTypeEnum.isTypeAchievedInteger(
                                                achievedChallenge.challenge.type
                                            )
                                        ) {
                                            achievedToPrint =
                                                achievedChallenge.achieved.toInt().toString()
                                        }
                                        val achievedPrefix =
                                            "\n\nAchieved: ${achievedToPrint} ${achievedChallenge.challenge.type}s\n"
                                        if (completePercentage >= 1) {
                                            report += achievedPrefix + "████████████████████ 100%"
                                        } else {
                                            completePercentage *= 20
                                            val intCompletePercentage = completePercentage.toInt()
                                            var completionBar = ""
                                            for (i in 1..intCompletePercentage) {
                                                completionBar += "█"
                                            }
                                            for (i in 1..(20 - intCompletePercentage)) {
                                                completionBar += "░"
                                            }
                                            completionBar += " ${intCompletePercentage * 5}%"
                                            report += achievedPrefix + completionBar
                                        }
                                    }
                                    if (copyReportOnClipboard) {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                report
                                            )
                                        )
                                        Toast.makeText(
                                            localContext,
                                            "${sortableGameEvent.event.getEventTitle()} report copied",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            report
                                        )
                                        type = "text/plain"
                                    }
                                    var eventDescription =
                                        when (sortableGameEvent.classType) {
                                            AbstractSession::class.java.simpleName -> " session "
                                            Date::class.java.simpleName -> " date "
                                            SingleSet::class.java.simpleName -> " set "
                                            Challenge::class.java.simpleName -> " challenge "
                                            else -> " "
                                        }
                                    val shareIntent = Intent.createChooser(
                                        sendIntent,
                                        "Share${eventDescription}report"
                                    )
                                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    localContext.startActivity(shareIntent)
                                },
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Event"
                            )
                            IconShadowButton(
                                onClick = {
                                    if (AbstractSession::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.DeleteSession(
                                                sortableGameEvent.event as AbstractSession
                                            )
                                        )
                                    }
                                    if (SingleSet::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.DeleteSet(
                                                sortableGameEvent.event as SingleSet
                                            )
                                        )
                                    }
                                    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        onEvent(
                                            GameEvent.DeleteDate(
                                                sortableGameEvent.event as Date
                                            )
                                        )
                                    }
                                    if (AchievedChallenge::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        onEvent(
                                            GameEvent.DeleteChallenge(
                                                (sortableGameEvent.event as AchievedChallenge).challenge
                                            )
                                        )
                                    }
                                },
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Event",
                                iconColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                            IconShadowButton(
                                onClick = {
                                    onEvent(GameEvent.EmptyLeads)
                                    if (AbstractSession::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        leads.forEach { lead ->
                                            onEvent(
                                                GameEvent.SetLead(
                                                    lead
                                                )
                                            )
                                        }
                                        onEvent(GameEvent.SetIsInOverlayToTrue)
                                        onEvent(
                                            GameEvent.EditSession(
                                                sortableGameEvent.event as AbstractSession
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.SESSION
                                            )
                                        )
                                    }
                                    if (SingleSet::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.SetConversation((sortableGameEvent.event as SingleSet).conversation)
                                        )
                                        onEvent(
                                            GameEvent.SetContact((sortableGameEvent.event as SingleSet).contact)
                                        )
                                        onEvent(
                                            GameEvent.SetInstantDate((sortableGameEvent.event as SingleSet).instantDate)
                                        )
                                        onEvent(
                                            GameEvent.SetRecorded((sortableGameEvent.event as SingleSet).recorded)
                                        )
                                        onEvent(GameEvent.SetIsInOverlayToTrue)
                                        onEvent(
                                            GameEvent.EditSet(
                                                sortableGameEvent.event as SingleSet
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.SET
                                            )
                                        )
                                    }
                                    if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                        onEvent(
                                            GameEvent.SetPull((sortableGameEvent.event as Date).pull)
                                        )
                                        onEvent(
                                            GameEvent.SetBounce((sortableGameEvent.event as Date).bounce)
                                        )
                                        onEvent(
                                            GameEvent.SetKiss((sortableGameEvent.event as Date).kiss)
                                        )
                                        onEvent(
                                            GameEvent.SetLay((sortableGameEvent.event as Date).lay)
                                        )
                                        onEvent(
                                            GameEvent.SetRecorded((sortableGameEvent.event as Date).recorded)
                                        )
                                        onEvent(GameEvent.SetIsInOverlayToTrue)
                                        onEvent(
                                            GameEvent.EditDate(
                                                sortableGameEvent.event as Date
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.DATE
                                            )
                                        )
                                    }
                                    if (AchievedChallenge::class.java.simpleName.equals(
                                            sortableGameEvent.classType
                                        )
                                    ) {
                                        onEvent(
                                            GameEvent.SetChallengeName((sortableGameEvent.event as AchievedChallenge).challenge.name!!)
                                        )
                                        onEvent(
                                            GameEvent.SetChallengeDescription((sortableGameEvent.event as AchievedChallenge).challenge.description!!)
                                        )
                                        onEvent(
                                            GameEvent.SetChallengeStartDate((sortableGameEvent.event as AchievedChallenge).challenge.startDate)
                                        )
                                        val startDate =
                                            FormatService.parseDate((sortableGameEvent.event as AchievedChallenge).challenge.startDate)
                                        val endDate =
                                            FormatService.parseDate((sortableGameEvent.event as AchievedChallenge).challenge.endDate)
                                        val duration = ChronoUnit.DAYS.between(startDate, endDate)
                                        onEvent(
                                            GameEvent.SetChallengeDuration(duration.toString())
                                        )
                                        onEvent(
                                            GameEvent.SetChallengeType((sortableGameEvent.event as AchievedChallenge).challenge.type)
                                        )
                                        onEvent(
                                            GameEvent.SetChallengeGoal((sortableGameEvent.event as AchievedChallenge).challenge.goal.toString())
                                        )
                                        onEvent(
                                            GameEvent.SetChallengeTweetUrl((sortableGameEvent.event as AchievedChallenge).challenge.tweetUrl!!)
                                        )
                                        onEvent(GameEvent.SetIsInOverlayToTrue)
                                        onEvent(
                                            GameEvent.EditChallenge(
                                                (sortableGameEvent.event as AchievedChallenge).challenge
                                            )
                                        )
                                        onEvent(
                                            GameEvent.ShowDialog(
                                                false,
                                                true,
                                                EventTypeEnum.CHALLENGE
                                            )
                                        )
                                    }
                                },
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Event"
                            )
                        }
                    }
                    LittleBodyText(sortableGameEvent.event.getEventDuration())
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        CardSection {
                            if (AbstractSession::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                SessionBody(
                                    sortableGameEvent.event as AbstractSession,
                                    50.sp,
                                    10.sp,
                                    15.sp
                                )
                            }
                            if (SingleSet::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                SetBody(sortableGameEvent.event as SingleSet, 10.sp, 15.sp)
                            }
                            if (Date::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                DateBody(sortableGameEvent.event as Date, 10.sp, 15.sp)
                            }
                            if (AchievedChallenge::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                ChallengeBody(
                                    sortableGameEvent.event as AchievedChallenge,
                                    40.sp,
                                    10.sp
                                )
                            }
                        }
                        if (!AchievedChallenge::class.java.simpleName.equals(sortableGameEvent.classType)) {
                            Spacer(modifier = Modifier.height(7.dp))
                            val semiOpaqueBackground = MaterialTheme.colorScheme.surface
                            CardSection {
                                if (leads == null || leads.isEmpty()) {
                                    LittleBodyText("No leads")
                                } else {
                                    LittleBodyText("Leads:")
                                    Spacer(modifier = Modifier.height(7.dp))
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .drawWithContent {
                                                drawContent()
                                                drawRect(
                                                    brush = getEventCardLeadsBrush(
                                                        semiOpaqueBackground
                                                    )
                                                )
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        item {
                                            Spacer(modifier = Modifier.width(15.dp))
                                        }
                                        for (lead in leads) {
                                            item {
                                                Row(
                                                    modifier = Modifier.clickable {
                                                        onEvent(GameEvent.SetIsInOverlayToTrue)
                                                        onEvent(
                                                            GameEvent.EditLead(
                                                                lead, true
                                                            )
                                                        )
                                                        onEvent(
                                                            GameEvent.ShowLeadDialog(
                                                                false, false
                                                            )
                                                        )
                                                    },
                                                    horizontalArrangement = Arrangement.spacedBy(
                                                        7.dp
                                                    )
                                                ) {
                                                    leadName(
                                                        lead = lead,
                                                        backgroundColor = MaterialTheme.colorScheme.background,
                                                        outputShow = false,
                                                        cardShow = true
                                                    )
                                                }
                                            }
                                            item {
                                                Spacer(modifier = Modifier.width(5.dp))
                                            }
                                        }
                                        item {
                                            Spacer(modifier = Modifier.width(35.dp))
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            var stickingPoints =
                                sortableGameEvent.event.getEventStickingPoints()
                            val validStickingPoints =
                                stickingPoints != null && !stickingPoints.isBlank()
                            var width = 1f
                            if (validStickingPoints) {
                                width = 0.8f
                            }
                            var sectionDescription = "Sticking points"
                            if (AchievedChallenge::class.java.simpleName.equals(sortableGameEvent.classType)) {
                                sectionDescription = "Description"
                            }
                            CardSection(width = width) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    ) {
                                        val noSectionContentSentence =
                                            "No ${sectionDescription.lowercase()}"
                                        if (stickingPoints == null || stickingPoints.isBlank()) {
                                            LittleBodyText(noSectionContentSentence)
                                        } else {
                                            LittleBodyText("${sectionDescription}:")
                                            Spacer(modifier = Modifier.height(5.dp))
                                            MediumBodyText(if (stickingPoints != null) stickingPoints else noSectionContentSentence)
                                        }
                                    }
                                }
                            }
                            if (validStickingPoints) {
                                Spacer(modifier = Modifier.width(10.dp))
                                IconShadowButton(
                                    onClick = {
                                        if (stickingPoints != null) {
                                            clipboardManager.setText(
                                                AnnotatedString(
                                                    stickingPoints
                                                )
                                            )
                                            Toast.makeText(
                                                localContext,
                                                "Sticking points copied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Sticking Points"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getEventCardLeadsBrush(semiOpaqueBackground: Color): Brush {
    return Brush.horizontalGradient(
        0.0f to semiOpaqueBackground,
        0.01f to semiOpaqueBackground.copy(alpha = 0.95f),
        0.02f to semiOpaqueBackground.copy(alpha = 0.6f),
        0.03f to semiOpaqueBackground.copy(alpha = 0.2f),
        0.05f to semiOpaqueBackground.copy(alpha = 0f),
        0.88f to semiOpaqueBackground.copy(alpha = 0f),
        0.91f to semiOpaqueBackground.copy(alpha = 0.3f),
        0.94f to semiOpaqueBackground.copy(alpha = 0.65f),
        0.97f to semiOpaqueBackground.copy(alpha = 0.99f),
        1.0f to semiOpaqueBackground
    )
}