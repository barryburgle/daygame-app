package com.barryburgle.gameapp.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.output.sectionTitleAndDescription
import com.barryburgle.gameapp.ui.stats.section.LeadsHistogramsSection
import com.barryburgle.gameapp.ui.stats.section.SessionsHistogramsSection
import com.barryburgle.gameapp.ui.stats.state.StatsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    state: StatsState
) {
    val spaceFromTop = 20.dp
    val spaceFromBottom = 60.dp
    val heigh: Dp = 200.dp
    val width: Dp = 320.dp
    Scaffold { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsCard(
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
                    firstPerformanceQuantity = "${GlobalStatsService.computeAvgConvoRatio(state.abstractSessions)} %",
                    firstPerformanceDescription = "Conversation\nRatio",
                    secondPerformanceQuantity = "${GlobalStatsService.computeAvgRejectionRatio(state.abstractSessions)} %",
                    secondPerformanceDescription = "Rejection\nRatio",
                    thirdPerformanceQuantity = "${GlobalStatsService.computeAvgContactRatio(state.abstractSessions)} %",
                    thirdPerformanceDescription = "Contact\nRatio",
                    fourthPerformanceQuantity = "${GlobalStatsService.computeAvgIndex(state.abstractSessions)}",
                    fourthPerformanceDescription = "Average\nIndex"
                )
            }
            item {
                val numbers: Int =
                    state.leads.filter { lead -> lead.contact == ContactTypeEnum.NUMBER.getField() }.size
                val socials: Int =
                    state.leads.filter { lead -> lead.contact == ContactTypeEnum.SOCIAL.getField() }.size
                StatsCard(
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
            item {
                sectionTitleAndDescription(
                    "Sessions Histograms",
                    "Number of sessions in which you reached a certain amount of:"
                )
                LazyRow {
                    SessionsHistogramsSection(state, heigh, width)
                }
            }
            item {
                sectionTitleAndDescription(
                    "Leads Histograms",
                    "Number of leads with specific characteristics:"
                )
                LazyRow {
                    LeadsHistogramsSection(state, heigh, width)
                }
            }
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
        }
    }
}