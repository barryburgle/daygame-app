package com.barryburgle.gameapp.ui.stats

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
import com.barryburgle.gameapp.ui.stats.section.LeadsHistogramsSection
import com.barryburgle.gameapp.ui.stats.section.SessionsHistogramsSection
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.utilities.InsertInvite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    state: StatsState,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    val heigh: Dp = 200.dp
    val width: Dp = 320.dp
    Scaffold { padding ->
        InsertInvite(state.abstractSessions, "Session")
        if (state.abstractSessions.isNotEmpty()) {
            val cardModifier = Modifier
                .shadow(
                    elevation = 5.dp,
                    shape = MaterialTheme.shapes.large
                )
                .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        y = spaceFromTop + spaceFromLeft
                    ),
                verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
            ) {
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(spaceFromLeft)
                        )
                        StatsCard(
                            modifier = cardModifier,
                            title = "Overall",
                            description = "${GlobalStatsService.computeSpentHours(state.abstractSessions)} hours spent, on average a set each ${
                                GlobalStatsService.computeAvgApproachTime(
                                    state.abstractSessions
                                )
                            } minutes",
                            firstQuantifierQuantity = "${GlobalStatsService.computeSets(state.abstractSessions)}",
                            firstQuantifierDescription = "Sets",
                            secondQuantifierQuantity = "${GlobalStatsService.computeConvos(state.abstractSessions)}",
                            secondQuantifierDescription = "Conversations",
                            thirdQuantifierQuantity = "${GlobalStatsService.computeContacts(state.abstractSessions)}",
                            thirdQuantifierDescription = "Contacts",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeAvgConvoRatio(
                                    state.abstractSessions
                                )
                            } %",
                            firstPerformanceDescription = "Conversation\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeAvgRejectionRatio(
                                    state.abstractSessions
                                )
                            } %",
                            secondPerformanceDescription = "Rejection\nRatio",
                            thirdPerformanceQuantity = "${
                                GlobalStatsService.computeAvgContactRatio(
                                    state.abstractSessions
                                )
                            } %",
                            thirdPerformanceDescription = "Contact\nRatio",
                            fourthPerformanceQuantity = "${GlobalStatsService.computeAvgIndex(state.abstractSessions)}",
                            fourthPerformanceDescription = "Average\nIndex"
                        )
                    }
                }
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(spaceFromLeft)
                        )
                        val numbers: Int =
                            state.leads.filter { lead -> lead.contact == ContactTypeEnum.NUMBER.getField() }.size
                        val socials: Int =
                            state.leads.filter { lead -> lead.contact == ContactTypeEnum.SOCIAL.getField() }.size
                        StatsCard(
                            modifier = cardModifier,
                            title = "Leads",
                            description = "On average a new lead each ${
                                GlobalStatsService.computeAvgLeadTime(
                                    state.leads.size,
                                    state.abstractSessions
                                )
                            } minutes",
                            firstQuantifierQuantity = "${state.leads.size}",
                            firstQuantifierDescription = "Leads",
                            secondQuantifierQuantity = "${numbers}",
                            secondQuantifierDescription = "Numbers",
                            thirdQuantifierQuantity = "${socials}",
                            thirdQuantifierDescription = "Social Medias",
                            firstPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.leads.size,
                                    numbers
                                )
                            } %",
                            firstPerformanceDescription = "Number\nRatio",
                            secondPerformanceQuantity = "${
                                GlobalStatsService.computeGenericRatio(
                                    state.leads.size,
                                    socials
                                )
                            } %",
                            secondPerformanceDescription = "Social\nRatio"
                        )
                    }
                }
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(spaceFromLeft)
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
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Leads Histograms",
                            "Number of leads with specific characteristics:"
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
                item {
                    Spacer(
                        modifier = Modifier
                            .height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)
                    )
                }
            }
        }
    }
}