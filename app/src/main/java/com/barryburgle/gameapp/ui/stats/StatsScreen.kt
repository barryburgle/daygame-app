package com.barryburgle.gameapp.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.sectionTitleAndDescription
import com.barryburgle.gameapp.ui.stats.section.DatesHistogramsSection
import com.barryburgle.gameapp.ui.stats.section.LeadsHistogramsSection
import com.barryburgle.gameapp.ui.stats.section.SessionsHistogramsSection
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.utilities.InsertInvite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    state: StatsState, spaceFromLeft: Dp, spaceFromTop: Dp, spaceFromBottom: Dp
) {
    // TODO: for each one of the cards "Sessions", "Leads" and "Dates" the default version that should be displayed should be a contracted one with only absolute counts, leading to showing performances on a dropdown arrow touch
    val heigh: Dp = 200.dp
    val width: Dp = 320.dp
    Scaffold { padding ->
        InsertInvite(state)
        val cardModifier = Modifier
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop - 20.dp
                ), verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(20.dp)
                )
            }
            if (state.allSessions.isNotEmpty()) {
                val sets: Int = GlobalStatsService.computeSets(state.allSessions)
                val conversations: Int =
                    GlobalStatsService.computeConvos(state.allSessions)
                val contacts: Int = GlobalStatsService.computeContacts(state.allSessions)
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        StatsCard(
                            modifier = cardModifier,
                            title = "Sessions",
                            description = "${GlobalStatsService.computeSessionSpentHours(state.allSessions)} hours spent on sessions, on average a set each ${
                                GlobalStatsService.computeAvgApproachTime(
                                    state.allSessions
                                )
                            } minutes",
                            firstQuantifierQuantity = "${sets}",
                            firstQuantifierDescription = "Sets",
                            secondQuantifierQuantity = "${conversations}",
                            secondQuantifierDescription = "Conversations",
                            thirdQuantifierQuantity = "${contacts}",
                            thirdQuantifierDescription = "Contacts",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    sets,
                                    conversations
                                )
                            } %",
                            firstPerformanceDescription = "Conversation\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    sets,
                                    sets - conversations
                                )
                            } %",
                            secondPerformanceDescription = "Rejection\nRatio",
                            thirdPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(conversations, contacts)
                            } %",
                            thirdPerformanceDescription = "Contact\nRatio",
                            fourthPerformanceQuantity = "${
                                GlobalStatsService.computeAvgIndex(
                                    state.allSessions
                                )
                            }",
                            fourthPerformanceDescription = "Average\nIndex"
                        )
                    }
                }
            }
            if (state.allSets.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        val conversations: Int =
                            state.allSets.filter { set -> set.conversation }.size
                        val contacts: Int = state.allSets.filter { set -> set.contact }.size
                        val instantDates: Int =
                            state.allSets.filter { set -> set.instantDate }.size
                        val recorded: Int = state.allSets.filter { set -> set.recorded }.size
                        val avgContactTime: Long =
                            GlobalStatsService.computeAvgContactTime(state.allSets, contacts)
                        val contactSentence =
                            if (avgContactTime != 0L) "on average a contact each ${avgContactTime} minutes" else "no contacts yet"
                        val setSpentHours =
                            GlobalStatsService.computeSetsSpentHours(state.allSets)
                        val setSpentMinutes =
                            GlobalStatsService.computeSetsSpentMinutes(state.allSets)
                        var timeSpentSentence = "${setSpentMinutes} minutes"
                        if (setSpentHours != 0L) {
                            val minutesDifference = setSpentMinutes - setSpentHours * 60
                            timeSpentSentence =
                                "${setSpentHours} hours and " + "${minutesDifference} minutes"
                        }
                        StatsCard(
                            modifier = cardModifier,
                            title = "Single Sets",
                            description = "${timeSpentSentence} spent on single sets, " + contactSentence,
                            firstQuantifierQuantity = "${state.allSets.size}",
                            firstQuantifierDescription = "Sets",
                            secondQuantifierQuantity = "${conversations}",
                            secondQuantifierDescription = "Conversations",
                            thirdQuantifierQuantity = "${contacts}",
                            thirdQuantifierDescription = "Contacts",
                            fourthQuantifierQuantity = "${instantDates}",
                            fourthQuantifierDescription = "Instant\nDates",
                            fifthQuantifierQuantity = "${recorded}",
                            fifthQuantifierDescription = "Recorded\nSets",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allSets.size,
                                    conversations
                                ) //TODO: consider using always compute generic ratio and not other methods
                            } %",
                            firstPerformanceDescription = "Conversation\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allSets.size,
                                    contacts
                                )
                            } %",
                            secondPerformanceDescription = "Contact\nRatio",
                            thirdPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allSets.size,
                                    instantDates
                                )
                            } %",
                            thirdPerformanceDescription = "iDate\nRatio"
                        )
                    }
                }
            }
            if (state.allLeads.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        val numbers: Int =
                            state.allLeads.filter { lead -> lead.contact == ContactTypeEnum.NUMBER.getField() }.size
                        val socials: Int =
                            state.allLeads.filter { lead -> lead.contact == ContactTypeEnum.SOCIAL.getField() }.size
                        val avgLeadTime: Long = GlobalStatsService.computeAvgLeadTime(
                            state.allLeads.size,
                            state.allSessions
                        )
                        StatsCard(
                            modifier = cardModifier,
                            title = "Leads",
                            description = if (avgLeadTime != 0L) "On average a new lead each ${avgLeadTime} minutes" else "No leads acquired yet",
                            firstQuantifierQuantity = "${state.allLeads.size}",
                            firstQuantifierDescription = "Leads",
                            secondQuantifierQuantity = "${numbers}",
                            secondQuantifierDescription = "Numbers",
                            thirdQuantifierQuantity = "${socials}",
                            thirdQuantifierDescription = "Social Medias",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allLeads.size, numbers
                                )
                            } %",
                            firstPerformanceDescription = "Number\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allLeads.size, socials
                                )
                            } %",
                            secondPerformanceDescription = "Social\nRatio",
                            thirdPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allLeads.size, state.allDates.size
                                )
                            } %",
                            thirdPerformanceDescription = "Date to Lead\nRatio",
                        )
                    }
                }
            }
            if (state.allDates.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        val pulls: Int = state.allDates.filter { date -> date.pull }.size
                        val bounces: Int = state.allDates.filter { date -> date.bounce }.size
                        val kisses: Int = state.allDates.filter { date -> date.kiss }.size
                        val lays: Int = state.allDates.filter { date -> date.lay }.size
                        val avgLayTime: Long =
                            GlobalStatsService.computeAvgLayTime(state.allDates, lays)
                        val layTimeSentence =
                            if (avgLayTime != 0L) "on average a lay each ${avgLayTime} minutes" else "no lays yet"
                        StatsCard(
                            modifier = cardModifier,
                            title = "Dates",
                            description = "${GlobalStatsService.computeDateSpentHours(state.allDates)} hours spent on dates, " + layTimeSentence,
                            firstQuantifierQuantity = "${state.allDates.size}",
                            firstQuantifierDescription = "Dates",
                            secondQuantifierQuantity = "${pulls}",
                            secondQuantifierDescription = "Pulls",
                            thirdQuantifierQuantity = "${bounces}",
                            thirdQuantifierDescription = "Bounces",
                            fourthQuantifierQuantity = "${kisses}",
                            fourthQuantifierDescription = "Kisses",
                            fifthQuantifierQuantity = "${lays}",
                            fifthQuantifierDescription = "Lays",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.allDates.size, pulls
                                )
                            } %",
                            firstPerformanceDescription = "Pull to Date\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    pulls, bounces
                                )
                            } %",
                            secondPerformanceDescription = "Bounce to Pull\nRatio",
                            thirdPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    bounces, kisses
                                )
                            } %",
                            thirdPerformanceDescription = "Kiss to Bounce\nRatio",
                            fourthPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    kisses, lays
                                )
                            } %",
                            fourthPerformanceDescription = "Lay to Kiss\nRatio"
                        )
                    }
                }
            }
            if (state.setsHistogram.isNotEmpty() && state.convosHistogram.isNotEmpty() && state.contactsHistogram.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Sessions Histograms",
                            "Number of sessions in which you reached a certain amount of:"
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                        SessionsHistogramsSection(state, heigh, width)
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
            }
            if (state.leadsNationalityHistogram.isNotEmpty() && state.leadsAgeHistogram.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Leads Histograms", "Number of leads with specific characteristics:"
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                        LeadsHistogramsSection(state, heigh, width)
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
            }
            if (state.datesNationalityHistogram.isNotEmpty() && state.datesAgeHistogram.isNotEmpty() && state.datesNumberHistogram.isNotEmpty()) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Dates Histograms", "Number of dates with specific characteristics:"
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                        DatesHistogramsSection(
                            state, heigh, width
                        )
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)
                )
            }
        }
    }
}