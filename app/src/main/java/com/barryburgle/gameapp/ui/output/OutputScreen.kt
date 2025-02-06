package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutputScreen(
    state: OutputState
) {
    val spaceFromTop = 20.dp
    val spaceFromBottom = 60.dp // TODO: centralize across screens
    // TODO: make cards with injectable type of charts
    // TODO: make different types of charts injectable with arrays
    // TODO: substitute the following with table fetch
    Scaffold { padding ->
        InsertInvite(state.abstractSessions, "Session")
        if (state.abstractSessions.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = spaceFromTop),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    sectionTitleAndDescription(
                        "Leads", "Remember about your last fruitful meetings:"
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                    }
                }
                val heigh: Dp = 200.dp
                val width: Dp = 320.dp
                item {
                    sectionTitleAndDescription(
                        "Sessions", "Observe your progress through sessions:"
                    )
                    LazyRow {
                        SessionSection(state, heigh, width)
                    }
                }
                item {
                    sectionTitleAndDescription(
                        "Weeks", "Observe your progress through weeks:"
                    )
                    LazyRow {
                        WeekSection(state, heigh, width)
                    }
                }
                item {
                    sectionTitleAndDescription(
                        "Months", "Observe your progress through months:"
                    )
                    LazyRow {
                        MonthSection(state, heigh, width)
                    }
                }
                item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
            }
        }
    }
}

@Composable
fun sectionTitleAndDescription(
    title: String, description: String
) {
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

@Composable
fun getLeadAlertColor(lead: Lead): Color {
    val now = OffsetDateTime.now()
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
