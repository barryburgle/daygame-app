package com.barryburgle.gameapp.ui.input.card

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.model.stat.AggregatedPeriod
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.card.body.SummaryBody
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@ExperimentalMaterial3Api
@Composable
fun SummaryCard(
    state: InputState,
    modifier: Modifier = Modifier,
) {
    // TODO: draw tow little charts on the edge right side of each week/month section for the three sets/contats/series in the last two weeks/months (fading away on left)
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    val noEvents = state.allEvents.isEmpty()
    var updatedDate = ""
    var weekSets = 0
    var weekContacts = 0
    var weekSessionHours = 0L
    var weekDates = 0
    var weekDateHours = 0L
    var monthSets = 0
    var monthContacts = 0
    var monthSessionHours = 0L
    var monthDates = 0
    var monthDateHours = 0L
    if (!noEvents) {
        updatedDate = FormatService.getDate(state.allEvents.first().eventDate)
        val aggregatedWeekPeriodsList: List<AggregatedPeriod> =
            SessionManager.createAggregatedPeriodList(
                state.sessionsByWeek,
                state.datesByWeek
            )
        val aggregatedWeekSessions =
            SessionManager.getAggregatedSessions(aggregatedWeekPeriodsList)
        val aggregatedWeekDates =
            SessionManager.getAggregatedDates(aggregatedWeekPeriodsList)
        val aggregatedMonthPeriodsList: List<AggregatedPeriod> =
            SessionManager.createAggregatedPeriodList(
                state.sessionsByMonth,
                state.datesByMonth
            )
        val aggregatedMonthSessions =
            SessionManager.getAggregatedSessions(aggregatedMonthPeriodsList)
        val aggregatedMonthDates =
            SessionManager.getAggregatedDates(aggregatedMonthPeriodsList)

        if (aggregatedWeekSessions.isNotEmpty()) {
            weekSets = aggregatedWeekSessions.last().sets.toInt()
            weekContacts = aggregatedWeekSessions.last().contacts.toInt()
            weekSessionHours =
                aggregatedWeekSessions.last().timeSpent.toLong()
        }
        if (aggregatedWeekDates.isNotEmpty()) {
            weekDates = aggregatedWeekDates.last().dates.toInt()
            weekDateHours =
                aggregatedWeekDates.last().dateTimeSpent.toLong()
        }
        if (aggregatedMonthSessions.isNotEmpty()) {
            monthSets = aggregatedMonthSessions.last().sets.toInt()
            monthContacts = aggregatedMonthSessions.last().contacts.toInt()
            monthSessionHours =
                aggregatedMonthSessions.last().timeSpent.toLong()
        }
        if (aggregatedMonthDates.isNotEmpty()) {
            monthDates = aggregatedMonthDates.last().dates.toInt()
            monthDateHours =
                aggregatedMonthDates.last().dateTimeSpent.toLong()
        }
    }
    val weekTimeSpent =
        "$weekSessionHours hours spent over sessions and $weekDateHours over dates in the last week"
    val monthTimeSpent =
        "$monthSessionHours hours spent over sessions and $monthDateHours over dates in the last month."
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (noEvents) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        LittleBodyText("No events for summary")
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        LittleBodyText("Updated to $updatedDate")
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
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Summary",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.height(25.dp)
                                )
                                Spacer(modifier = Modifier.width(7.dp))
                                LargeTitleText("Summary")
                            }
                            Row(
                                modifier = Modifier
                                    .width(60.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconShadowButton(
                                    onClick = {
                                        var histogramData = exportSummary(
                                            updatedDate,
                                            weekSets,
                                            weekContacts,
                                            weekDates,
                                            monthSets,
                                            monthContacts,
                                            monthDates,
                                            weekTimeSpent,
                                            monthTimeSpent
                                        )
                                        if (state.copyReportOnClipboard) {
                                            clipboardManager.setText(
                                                AnnotatedString(
                                                    histogramData
                                                )
                                            )
                                            Toast.makeText(
                                                localContext,
                                                "Summary copied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                histogramData
                                            )
                                            type = "text/plain"
                                        }
                                        val shareIntent = Intent.createChooser(
                                            sendIntent,
                                            "Share summary"
                                        )
                                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        localContext.startActivity(shareIntent)
                                    },
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share Summary"
                                )
                            }
                        }
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
                                SummaryBody(
                                    "Last week",
                                    weekTimeSpent,
                                    weekSets,
                                    weekContacts,
                                    weekDates,
                                    40.sp,
                                    10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(7.dp))
                            CardSection {
                                SummaryBody(
                                    "Last month",
                                    monthTimeSpent,
                                    monthSets,
                                    monthContacts,
                                    monthDates,
                                    40.sp,
                                    10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun exportSummary(
    state: InputState,
    updatedDate: String,
    weekSets: Int,
    weekContacts: Int,
    weekDates: Int,
    monthSets: Int,
    monthContacts: Int,
    monthDates: Int,
    weekTimeDescription: String,
    monthTimeDescription: String,
    achievedChallenge: AchievedChallenge,
    isChallengeValid: Boolean
): String {
    var summary = "\uD83D\uDCCC $updatedDate summary:\n\n"
    var lastWeek =
        "• Current week:\n-$weekSets sets\n-$weekContacts contacts\n-$weekDates dates\n-$weekTimeDescription\n\n"
    var lastMonth =
        "• Current month:\n-$monthSets sets\n-$monthContacts contacts\n-$monthDates dates\n-$monthTimeDescription\n\n"
    var currentChallenge = achievedChallenge.getAchievedChallengeReport(true)
    if (state.showCurrentWeekSummary) {
        summary += lastWeek
    }
    if (state.showCurrentMonthSummary) {
        summary += lastMonth
    }
    if (state.showCurrentChallengeSummary && isChallengeValid) {
        summary += currentChallenge
    }
    return summary
}