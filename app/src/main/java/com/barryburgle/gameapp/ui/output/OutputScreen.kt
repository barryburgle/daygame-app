package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.leadName
import com.barryburgle.gameapp.ui.output.section.MonthSection
import com.barryburgle.gameapp.ui.output.section.SessionSection
import com.barryburgle.gameapp.ui.output.section.WeekSection
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.theme.AlertHigh
import com.barryburgle.gameapp.ui.theme.AlertLow
import com.barryburgle.gameapp.ui.theme.AlertMid
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutputScreen(
    state: OutputState,
    onEvent: (OutputEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    // TODO: make cards with injectable type of charts
    // TODO: make different types of charts injectable with arrays
    Scaffold { padding ->
        InsertInvite(state.abstractSessions, "Session")
        if (state.abstractSessions.isNotEmpty()) {
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
                                .background(MaterialTheme.colorScheme.background)
                                .width(spaceFromLeft)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            sectionTitleAndDescription(
                                "Leads", "Remember about your last fruitful meetings:"
                            )
                            Row(
                                modifier = Modifier.width(75.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Legend",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                IconButton(onClick = {
                                    onEvent(OutputEvent.SwitchShowLeadLegend)
                                }) {
                                    Icon(
                                        imageVector = if (state.showLeadsLegend) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "Leads legend",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .height(50.dp)
                                    )
                                }
                            }
                        }
                    }
                    BasicAnimatedVisibility(
                        visibilityFlag = state.showLeadsLegend,
                    ) {
                        Row {
                            Spacer(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .width(spaceFromLeft)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                legendLead("0 - 4 days ago", AlertLow)
                                Spacer(modifier = Modifier.width(18.dp))
                                legendLead("5 - 7 days ago", AlertMid)
                                Spacer(modifier = Modifier.width(18.dp))
                                legendLead("8 + days ago", AlertHigh)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                        for (lead in state.leads) {
                            item {
                                Row {
                                    leadName(
                                        lead = lead,
                                        backgroundColor = MaterialTheme.colorScheme.surface,
                                        alertColor = getLeadAlertColor(lead),
                                        outputShow = true,
                                        cardShow = false
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(
                                modifier = Modifier.width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
                val heigh: Dp = 200.dp
                val width: Dp = 320.dp
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .width(spaceFromLeft)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            sectionTitleAndDescription(
                                "Sessions", "Observe your progress through sessions:"
                            )
                            Row(
                                modifier = Modifier.width(110.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Index Formula",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                IconButton(onClick = {
                                    onEvent(OutputEvent.SwitchShowIndexFormula)
                                }) {
                                    Icon(
                                        imageVector = if (state.showIndexFormula) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "Index Formula",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .height(50.dp)
                                    )
                                }
                            }
                        }
                    }
                    BasicAnimatedVisibility(
                        visibilityFlag = state.showIndexFormula,
                    ) {
                        Row {
                            Spacer(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .width(spaceFromLeft)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Sets * (12 * Sets + 20 * Conversations + 30 * Contacts)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(0.85f)
                                            .height(1.dp)
                                            .background(color = MaterialTheme.colorScheme.onSurface)
                                    ) {}
                                    Text(
                                        text = "Session Time [minutes]",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
                            )
                        }
                        SessionSection(state, heigh, width)
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Weeks", "Observe your progress through weeks:"
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
                            )
                        }
                        WeekSection(state, heigh, width)
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
                            )
                        }
                    }
                }
                item {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .width(spaceFromLeft)
                        )
                        sectionTitleAndDescription(
                            "Months", "Observe your progress through months:"
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
                            )
                        }
                        MonthSection(state, heigh, width)
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(spaceFromLeft - 7.dp)
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

@Composable
fun sectionTitleAndDescription(
    title: String, description: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun getLeadAlertColor(lead: Lead): Color {
    // TODO: color and leadName date should not come from insert time but from session date
    val now = OffsetDateTime.now()
    if (lead.insertTime == null || lead.insertTime.isEmpty()) {
        return AlertHigh
    }
    val leadInsertTime = FormatService.parseDate(lead.insertTime.substring(0, 16) + "Z")
    val daysDifference = ChronoUnit.DAYS.between(leadInsertTime, now)
    if (daysDifference > 7) {
        return AlertHigh
    }
    if (daysDifference > 4) {
        return AlertMid
    }
    return AlertLow
}

@Composable
fun legendLead(legend: String, legendColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .width(10.dp)
                .height(10.dp)
                .background(legendColor, shape = RoundedCornerShape(10.dp))
        ) {}
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = legend,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}