package com.barryburgle.gameapp.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.service.GlobalStatsService
import com.barryburgle.gameapp.ui.input.describedQuantifier
import com.barryburgle.gameapp.ui.stats.state.StatsState

@ExperimentalMaterial3Api
@Composable
fun StatsCard(
    state: StatsState,
    modifier: Modifier = Modifier
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        )
) {
    val countFontSize = 50.sp
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Overall",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        text = "${GlobalStatsService.computeSpentHours(state.abstractSessions)} hours spent, on average a set each ${
                            GlobalStatsService.computeAvgApproachTime(
                                state.abstractSessions
                            )
                        } minutes",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeSets(state.abstractSessions)}",
                                quantityFontSize = countFontSize,
                                description = "Sets",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeConvos(state.abstractSessions)}",
                                quantityFontSize = countFontSize,
                                description = "Conversations",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeContacts(state.abstractSessions)}",
                                quantityFontSize = countFontSize,
                                description = "Contacts",
                                descriptionFontSize = descriptionFontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeAvgConvoRatio(state.abstractSessions)} %",
                                quantityFontSize = perfFontSize,
                                description = "Conversation\nRatio",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeAvgRejectionRatio(state.abstractSessions)} %",
                                quantityFontSize = perfFontSize,
                                description = "Rejection\nRatio",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeAvgContactRatio(state.abstractSessions)} %",
                                quantityFontSize = perfFontSize,
                                description = "Contact\nRatio",
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = "${GlobalStatsService.computeAvgIndex(state.abstractSessions)}",
                                quantityFontSize = perfFontSize,
                                description = "Average\nIndex",
                                descriptionFontSize = descriptionFontSize
                            )
                        }
                    }
                }
            }
        }
    }
}