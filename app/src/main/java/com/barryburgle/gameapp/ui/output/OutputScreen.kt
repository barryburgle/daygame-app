package com.barryburgle.gameapp.ui.output

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.HeatmapEntityEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.LeadDialog
import com.barryburgle.gameapp.ui.input.dialog.leadName
import com.barryburgle.gameapp.ui.output.section.MonthSection
import com.barryburgle.gameapp.ui.output.section.SessionSection
import com.barryburgle.gameapp.ui.output.section.WeekSection
import com.barryburgle.gameapp.ui.output.state.OutputState
import com.barryburgle.gameapp.ui.theme.AlertHigh
import com.barryburgle.gameapp.ui.theme.AlertLow
import com.barryburgle.gameapp.ui.theme.AlertMid
import com.barryburgle.gameapp.ui.tool.ScrollableSelector
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.BlurStatusBar
import com.barryburgle.gameapp.ui.utilities.InsertInvite
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText
import java.time.LocalDate
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
    val localContext = LocalContext.current.applicationContext
    val uriHandler = LocalUriHandler.current
    var heatmapEntitySelected by remember { mutableStateOf(HeatmapEntityEnum.SETS) }
    var isCustomSummaryMode by remember { mutableStateOf(false) }
    val blurBackground by animateDpAsState(
        targetValue = if (state.isInOverlay || state.isUpdatingLead) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 350),
        label = "blurBackground"
    )
    Scaffold(
        topBar = {
            BlurStatusBar()
        },
    ) { padding ->
        if (state.isUpdatingLead) {
            LeadDialog(
                state = state,
                onEvent = onEvent,
                description = "Update the lead"
            )
        }
        InsertInvite(state, blurBackground)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurBackground)
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
                                "History", "Have a look at your past:"
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconShadowButton(
                                        onClick = {
                                            isCustomSummaryMode = !isCustomSummaryMode
                                            if (isCustomSummaryMode) {
                                                Toast.makeText(
                                                    localContext,
                                                    "Select start date",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Range",
                                        iconColor = if (isCustomSummaryMode) MaterialTheme.colorScheme.onSurfaceVariant else null,
                                    )
                                    Spacer(modifier = Modifier.width(spaceFromLeft))
                                }
                            }
                        }
                    }
                    Row {
                        ScrollableSelector(
                            spaceFromLeft, HeatmapEntityEnum.values(), heatmapEntitySelected
                        ) { newValue ->
                            heatmapEntitySelected = newValue as HeatmapEntityEnum
                        }
                    }
                    val leadsMap = state.allLeads.associateBy { it.id }
                    val sessionsByDate =
                        state.allSessions.groupBy { FormatService.parseDate(it.date) }
                    val setsByDate = state.allSets.groupBy { FormatService.parseDate(it.date) }
                    val datesByDate = state.allDates.filter { it.date != null }
                        .groupBy { FormatService.parseDate(it.date!!) }
                    HeatmapCalendar(
                        modifier = Modifier.fillMaxWidth(),
                        entries = getSeries(
                            state,
                            heatmapEntitySelected,
                            sessionsByDate,
                            setsByDate,
                            datesByDate,
                            leadsMap
                        ),
                        spaceFromLeft = spaceFromLeft + 3.dp,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        cellColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        emptyColor = MaterialTheme.colorScheme.surface,
                        outputState = state,
                        isCustomSummaryMode = isCustomSummaryMode,
                        onOutputEvent = { onEvent(OutputEvent.SwitchShowCustomSummaryDialog) }
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
                                Row(
                                    modifier = Modifier.combinedClickable(
                                        onClick = {
                                            onEvent(OutputEvent.SetIsInOverlayToTrue)
                                            onEvent(OutputEvent.EditLead(lead, true))
                                        },
                                        onLongClick = {
                                            if (lead.contact == ContactTypeEnum.NUMBER.getField() && lead.contactLookupKey != null) {
                                                try {
                                                    val uri = Uri.withAppendedPath(
                                                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                                        lead.contactLookupKey
                                                    )
                                                    uriHandler.openUri(uri.toString())
                                                } catch (e: Exception) {
                                                    Toast.makeText(
                                                        localContext,
                                                        "Could not open contact",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else if (lead.contact == ContactTypeEnum.SOCIAL.getField() && lead.instagramUrl != null && lead.instagramUrl!!.isNotBlank()) {
                                                uriHandler.openUri(lead.instagramUrl!!)
                                            } else {
                                                Toast.makeText(
                                                    localContext,
                                                    "No contact found",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        7.dp
                                    )
                                ) {
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
        MediumTitleText(title, true)
        Spacer(modifier = Modifier.height(5.dp))
        LittleBodyText(description, italic = true)
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

fun getSeries(
    state: OutputState,
    heatmapEntity: HeatmapEntityEnum,
    sessionsByDate: Map<LocalDate, List<AbstractSession>>,
    setsByDate: Map<LocalDate, List<SingleSet>>,
    datesByDate: Map<LocalDate, List<Date>>,
    leadsMap: Map<Long, Lead>
): List<ContributionEntry> {
    return when (heatmapEntity) {
        HeatmapEntityEnum.SETS -> {
            val allDates = (sessionsByDate.keys + setsByDate.keys).distinct()
            allDates.map { date ->
                val sessions = sessionsByDate[date] ?: emptyList()
                val sets = setsByDate[date] ?: emptyList()

                var desc = ""
                var sessionSetsSum = 0.0f
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
                var singleSetsSum = 0
                for (set in sets) {
                    singleSetsSum += 1
                    desc += "\n[Single Set] ${FormatService.getTime(set.startHour)} - ${
                        FormatService.getTime(
                            set.endHour
                        )
                    }: 1 set"
                }
                ContributionEntry(date, sessionSetsSum + singleSetsSum, desc)
            }
        }

        HeatmapEntityEnum.CONVERSATIONS -> {
            val allDates = (sessionsByDate.keys + setsByDate.keys).distinct()
            allDates.map { date ->
                val sessions = sessionsByDate[date] ?: emptyList()
                val sets = setsByDate[date] ?: emptyList()

                var desc = ""
                var sessionConvosSum = 0.0f
                for (session in sessions) {
                    if (session.convos > 0) {
                        sessionConvosSum += session.convos
                        desc += "\n[Session] ${FormatService.getTime(session.startHour)} - ${
                            FormatService.getTime(
                                session.endHour
                            )
                        }: ${session.convos} conversations"
                    }
                }
                val singleConvosSum = sets.count { it.conversation }
                if (singleConvosSum > 0) {
                    desc += "\n[Single Conversations] $singleConvosSum"
                }
                ContributionEntry(date, sessionConvosSum + singleConvosSum, desc)
            }
        }

        HeatmapEntityEnum.CONTACTS -> {
            val allDates = (sessionsByDate.keys + setsByDate.keys).distinct()
            allDates.map { date ->
                val sessions = sessionsByDate[date] ?: emptyList()
                val sets = setsByDate[date] ?: emptyList()

                var desc = ""
                var sessionContactsSum = 0.0f
                for (session in sessions) {
                    if (session.contacts > 0) {
                        sessionContactsSum += session.contacts
                        desc += "\n[Session] ${FormatService.getTime(session.startHour)} - ${
                            FormatService.getTime(
                                session.endHour
                            )
                        }: ${session.contacts} contacts"
                    }
                }
                val singleContactsSum = sets.count { it.contact }
                if (singleContactsSum > 0) {
                    desc += "\n[Single Contact] $singleContactsSum"
                }
                ContributionEntry(date, sessionContactsSum + singleContactsSum, desc)
            }
        }

        HeatmapEntityEnum.INDEX -> {
            sessionsByDate.map { (date, sessions) ->
                val indexAvg = sessions.map { it.index }.average().toFloat()
                val desc = when {
                    sessions.size == 1 -> "\nIndex: $indexAvg"
                    sessions.size > 1 -> "\n[${sessions.size} sessions] Avg index: $indexAvg"
                    else -> ""
                }
                ContributionEntry(date = date, count = indexAvg, desc = desc)
            }
        }

        HeatmapEntityEnum.DATES -> {
            datesByDate.mapNotNull { (date, dates) ->
                var dateCount = 0.0f
                var desc = ""
                for (singleDate in dates) {
                    dateCount += 1.0f
                    val dateLead = singleDate.leadId?.let { leadsMap[it] }
                    if (dateLead != null) {
                        desc += "\n[${CountryEnum.getFlagByAlpha3(dateLead.nationality)} ${dateLead.name}] ${singleDate.dateType.replaceFirstChar { it.uppercase() }} ${
                            FormatService.getTime(
                                singleDate.startHour
                            )
                        } - ${FormatService.getTime(singleDate.endHour)}"
                    }
                }
                ContributionEntry(date = date, count = dateCount, desc = desc)
            }
        }

        HeatmapEntityEnum.RECORDINGS -> {
            val allDates = (datesByDate.keys + setsByDate.keys).distinct()
            allDates.map { date ->
                val dates = datesByDate[date] ?: emptyList()
                val sets = setsByDate[date] ?: emptyList()

                var desc = ""
                var datesRecSum = 0.0f
                for (singleDate in dates) {
                    if (singleDate.recorded) {
                        datesRecSum += 1.0f
                        val dateLead = singleDate.leadId?.let { leadsMap[it] }
                        if (dateLead != null) {
                            desc += "\n[${CountryEnum.getFlagByAlpha3(dateLead.nationality)} ${dateLead.name}] ${singleDate.dateType.replaceFirstChar { it.uppercase() }} ${
                                FormatService.getTime(
                                    singleDate.startHour
                                )
                            } - ${FormatService.getTime(singleDate.endHour)}"
                        }
                    }
                }
                val setsRecSum = sets.count { it.recorded }
                if (setsRecSum > 0) {
                    desc += "\n[Single Recording] $setsRecSum"
                }
                ContributionEntry(date, datesRecSum + setsRecSum, desc)
            }
        }

        HeatmapEntityEnum.PULLED -> datesByDate.mapNotNull { (date, dates) ->
            getDateContributionEntry(dates, condition = Date::pull, state, date)
        }

        HeatmapEntityEnum.BOUNCED -> datesByDate.mapNotNull { (date, dates) ->
            getDateContributionEntry(dates, condition = Date::bounce, state, date)
        }

        HeatmapEntityEnum.KISSED -> datesByDate.mapNotNull { (date, dates) ->
            getDateContributionEntry(dates, condition = Date::kiss, state, date)
        }

        HeatmapEntityEnum.LAID -> datesByDate.mapNotNull { (date, dates) ->
            getDateContributionEntry(dates, condition = Date::lay, state, date)
        }
    }
}

private fun getDateContributionEntry(
    dates: List<Date>,
    condition: (Date) -> Boolean,
    state: OutputState,
    date: LocalDate?
): ContributionEntry {
    var recCount = 0.0f
    var desc = ""
    for (singleDate in dates) {
        if (condition(singleDate)) {
            recCount += 1.0f
            var dateLead: Lead? = null
            for (lead in state.allLeads) {
                if (singleDate.leadId == lead.id) {
                    dateLead = lead
                }
            }
            desc += "\n[${CountryEnum.getFlagByAlpha3(dateLead!!.nationality)} ${dateLead!!.name}] ${singleDate.dateType.replaceFirstChar { it.uppercase() }} ${
                FormatService.getTime(
                    singleDate.startHour
                )
            } - ${
                FormatService.getTime(
                    singleDate.endHour
                )
            }"
        }
    }
    return ContributionEntry(
        date = date!!,
        count = recCount,
        desc
    )
}