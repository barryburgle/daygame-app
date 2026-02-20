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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.enums.HeatmapEntityEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.dialog.leadName
import com.barryburgle.gameapp.ui.output.section.MonthSection
import com.barryburgle.gameapp.ui.output.section.SessionSection
import com.barryburgle.gameapp.ui.output.section.WeekSection
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.theme.AlertHigh
import com.barryburgle.gameapp.ui.theme.AlertLow
import com.barryburgle.gameapp.ui.theme.AlertMid
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.BlurStatusBar
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText
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
    var heatmapEntitySelectorExpanded by remember { mutableStateOf(false) }
    var heatmapEntitySelected by remember { mutableStateOf(HeatmapEntityEnum.SETS) }
    Scaffold(
        topBar = {
            BlurStatusBar()
        },
    ) { padding ->
        InsertInvite(state)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop - 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            if (state.allSessions.isNotEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .height(40.dp)
                    )
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
                                "History", "Look back at your volume:"
                            )
                            Row(
                                modifier = Modifier.width(125.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                LittleBodyText(heatmapEntitySelected.getField())
                                IconButton(onClick = {
                                    heatmapEntitySelectorExpanded = !heatmapEntitySelectorExpanded
                                }) {
                                    Icon(
                                        imageVector = if (state.showLeadsLegend) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "Heatmap Entity Selection",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .height(50.dp)
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(450.dp),
                                    expanded = heatmapEntitySelectorExpanded,
                                    onDismissRequest = { heatmapEntitySelectorExpanded = false }
                                ) {
                                    HeatmapEntityEnum.values().forEach { enumValue ->
                                        DropdownMenuItem(
                                            text = { LittleBodyText(enumValue.getField()) },
                                            onClick = {
                                                heatmapEntitySelected = enumValue
                                                heatmapEntitySelectorExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    HeatmapCalendar(
                        modifier = Modifier.fillMaxWidth(),
                        entries = getSeries(state, heatmapEntitySelected),
                        selectEntity = heatmapEntitySelected,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        cellColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        emptyColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
            if (state.allLeads.isNotEmpty()) {
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
                                LittleBodyText("Legend")
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
                        for (lead in state.allLeads) {
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
            }
            if (state.allSessions.isNotEmpty()) {
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
                                LittleBodyText("Index Formula")
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
                                    LittleBodyText("Sets * (12 * Sets + 20 * Conversations + 30 * Contacts)")
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(0.85f)
                                            .height(1.dp)
                                            .background(color = MaterialTheme.colorScheme.onSurface)
                                    ) {}
                                    LittleBodyText("Session Time [minutes]")
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
        MediumTitleText(title)
        Spacer(modifier = Modifier.height(5.dp))
        LittleBodyText(description)
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
        LittleBodyText(legend)
    }
}

fun getSeries(state: OutputState, heatmapEntity: HeatmapEntityEnum): List<ContributionEntry> {
    return when (heatmapEntity) {
        HeatmapEntityEnum.SETS -> {
            val setsByDate = state.allSets
                .groupBy { FormatService.parseDate(it.date) }
                .mapValues { (_, sets) -> sets.size.toFloat() }
            return state.allSessionsUnlimited
                .groupBy { FormatService.parseDate(it.date) }
                .map { (date, sessions) ->
                    var sessionSetsSum = 0.0f
                    var desc = ""
                    for (session in sessions) {
                        sessionSetsSum += session.sets
                        if (session.sets > 0) {
                            desc += "\n[Session] ${FormatService.getTime(session.startHour)} - ${
                                FormatService.getTime(
                                    session.endHour
                                )
                            }: ${session.sets} sets"
                        }
                    }
                    val singleSetsSum = setsByDate[date] ?: 0f
                    if (singleSetsSum > 0) {
                        desc += "\n${singleSetsSum.toInt()} single sets"
                    }
                    ContributionEntry(
                        date,
                        sessionSetsSum + singleSetsSum,
                        desc
                    )
                }
        }

        HeatmapEntityEnum.CONVERSATIONS -> {
            val conversationsByDate = state.allSets
                .groupBy { FormatService.parseDate(it.date) }
                .mapValues { (_, sets) -> sets.count { entry -> entry.conversation }.toFloat() }
            return state.allSessionsUnlimited
                .groupBy { FormatService.parseDate(it.date) }
                .map { (date, sessions) ->
                    var sessionSetsSum = 0.0f
                    var desc = ""
                    for (session in sessions) {
                        sessionSetsSum += session.convos
                        if (session.convos > 0) {
                            desc += "\n[Session] ${FormatService.getTime(session.startHour)} - ${
                                FormatService.getTime(
                                    session.endHour
                                )
                            }: ${session.sets} conversations"
                        }
                    }
                    val singleSetsSum = conversationsByDate[date] ?: 0f
                    if (singleSetsSum > 0) {
                        desc += "\n${singleSetsSum.toInt()} single conversations"
                    }
                    ContributionEntry(
                        date,
                        sessionSetsSum + singleSetsSum,
                        desc
                    )
                }
        }

        HeatmapEntityEnum.CONTACTS -> {
            val contactsByDate = state.allSets
                .groupBy { FormatService.parseDate(it.date) }
                .mapValues { (_, sets) -> sets.count { entry -> entry.contact }.toFloat() }
            return state.allSessionsUnlimited
                .groupBy { FormatService.parseDate(it.date) }
                .map { (date, sessions) ->
                    var sessionSetsSum = 0.0f
                    var desc = ""
                    for (session in sessions) {
                        sessionSetsSum += session.contacts
                        if (session.contacts > 0) {
                            desc += "\n[Session] ${FormatService.getTime(session.startHour)} - ${
                                FormatService.getTime(
                                    session.endHour
                                )
                            }: ${session.sets} contacts"
                        }
                    }
                    val singleSetsSum = contactsByDate[date] ?: 0f
                    if (singleSetsSum > 0) {
                        desc += "\n${singleSetsSum.toInt()} single contacts"
                    }
                    ContributionEntry(
                        date,
                        sessionSetsSum + singleSetsSum,
                        desc
                    )
                }
        }

        HeatmapEntityEnum.INDEX -> state.allSessionsUnlimited
            .groupBy { FormatService.parseDate(it.date) }
            .map { (date, sessions) ->
                val indexAvg = sessions.map { it.index }.average().toFloat()
                var desc = ""
                if (sessions.size == 1) {
                    desc = "\nIndex: $indexAvg"
                } else if (sessions.size > 1) {
                    desc = "\n[${sessions.size} sessions] Avg index: $indexAvg"
                }
                ContributionEntry(
                    date = date,
                    count = indexAvg,
                    desc
                )
            }

        HeatmapEntityEnum.DATES -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.size.toFloat(),
                        ""
                    )
                }
            }

        // TODO: find a way to track in-session sets recordings: maybe when adding a convo or contact or lead allow option to flag a recording (or long press button) and write on another AbstractSession column the number of recordings
        // From session editing should be possible to edit the number of recordings
        HeatmapEntityEnum.RECORDINGS -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.count { entry -> entry.recorded }.toFloat(),
                        ""
                    )
                }
            }

        HeatmapEntityEnum.PULLED -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.count { entry -> entry.pull }.toFloat(),
                        ""
                    )
                }
            }

        HeatmapEntityEnum.BOUNCED -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.count { entry -> entry.bounce }.toFloat(),
                        ""
                    )
                }
            }

        HeatmapEntityEnum.KISSED -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.count { entry -> entry.kiss }.toFloat(),
                        ""
                    )
                }
            }

        HeatmapEntityEnum.LAID -> state.allDates
            .groupBy { it.date?.let { dateString -> FormatService.parseDate(dateString) } }
            .mapNotNull { (date, dates) ->
                date?.let {
                    ContributionEntry(
                        date = it,
                        count = dates.count { entry -> entry.lay }.toFloat(),
                        ""
                    )
                }
            }


        else -> emptyList()
    }
}