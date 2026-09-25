package com.barryburgle.gameapp.ui.output.dialog

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.ping.Ping
import com.barryburgle.gameapp.model.ping.SentPing
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.output.card.PingCard
import com.barryburgle.gameapp.ui.output.getLeadAlertColor
import com.barryburgle.gameapp.ui.output.icon.LeadContactButtonIcon
import com.barryburgle.gameapp.ui.utilities.animation.AnimatedStaggeredItem
import com.barryburgle.gameapp.ui.utilities.animation.VerticalProgressBarBrush
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.selection.DottedHorizontalPager
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText


@Composable
fun PingDialog(
    pings: List<Ping> = emptyList(),
    leads: List<Lead> = emptyList(),
    sentPings: List<SentPing> = emptyList(),
    onEvent: (OutputEvent) -> Unit
) {
    val sentPingsByLeadMap: Map<Long, List<SentPing>> = sentPings.groupBy { it.leadId }
    val pagerState = rememberPagerState(pageCount = { pings.size })
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val systemClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val uriHandler = LocalUriHandler.current
    FlowDialog(
        modifier = Modifier.fillMaxHeight(0.8f),
        onDismissRequest = { onEvent(OutputEvent.HidePingDialog) },
        onConfirm = { onEvent(OutputEvent.HidePingDialog) },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    LargeTitleText("Flight control", true)
                    WavyPlaceholder("Create, send and track pings")
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconShadowButton(
                        onClick = { onEvent(OutputEvent.ShowPingEditDialog) },
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add ping"
                    )
                }
            }
        }
    ) { contentPadding ->
        if (pings.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                WavyPlaceholder("Add some pings, they will come handy!")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(75.dp))
                DottedHorizontalPager(
                    items = pings,
                    modifier = Modifier
                        .fillMaxWidth(),
                    pageSpacing = 4.dp,
                    pagerState = pagerState
                ) { ping, page ->
                    AnimatedStaggeredItem(index = page - 1) {
                        PingCard(ping, onEvent)
                    }
                }
                Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                    WavyPlaceholder(
                        "Tick a box and the app will copy the ping to your clipboard, open the contact link and save the lead as pinged",
                        Modifier.fillMaxWidth()
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 24.dp) // Extra padding so top item isn't permanently obscured
                    ) {
                        items(
                            count = leads.size,
                            key = { index -> leads[index].id }
                        ) { index ->
                            val lead = leads[index]
                            val relativeIndex =
                                (index - listState.firstVisibleItemIndex).coerceAtLeast(0)
                            AnimatedStaggeredItem(index = relativeIndex) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val selectedPing = pings.getOrNull(pagerState.currentPage)
                                    val selectedPingId = selectedPing?.id
                                    var foundSentPing: SentPing? = null
                                    if (selectedPingId != null) {
                                        foundSentPing = getSentPingForLead(
                                            lead.id,
                                            selectedPingId,
                                            sentPingsByLeadMap
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(8.dp)
                                            .background(
                                                brush = VerticalProgressBarBrush(
                                                    getLeadAlertColor(lead)
                                                ),
                                                shape = RoundedCornerShape(
                                                    topStart = 10.dp,
                                                    bottomStart = 10.dp
                                                )
                                            )
                                    )
                                    // TODO: make this way of describing lead with flag-name-leadDesc centralized
                                    // (search for teh following line around)
                                    val leadAgeDesc = if (lead.age != 0L) " ${lead.age}" else ""
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(0.65f)
                                            .padding(vertical = 4.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        MediumTitleText(
                                            text = CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + leadAgeDesc
                                        )
                                        if (foundSentPing != null) {
                                            LittleBodyText(
                                                text = "Sent on ${
                                                    FormatService.getDate(
                                                        foundSentPing.sentHour
                                                    )
                                                } at ${
                                                    FormatService.getTime(
                                                        foundSentPing.sentHour
                                                    )
                                                }"
                                            )
                                        } else {
                                            LittleBodyText(text = "Never sent this ping")
                                        }
                                    }
                                    LeadContactButtonIcon(lead)
                                    if (selectedPingId != null && selectedPing != null) {
                                        Checkbox(
                                            checked = foundSentPing != null,
                                            onCheckedChange = { newlyChecked ->
                                                onEvent(
                                                    OutputEvent.WriteSentPing(
                                                        lead.id,
                                                        selectedPingId,
                                                        newlyChecked
                                                    )
                                                )
                                                if (newlyChecked) {
                                                    systemClipboard.setPrimaryClip(
                                                        selectedPing.getClipData(
                                                            context
                                                        )
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Ping copied to clipboard",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    if (lead.contact == ContactTypeEnum.NUMBER.getField() && lead.contactLookupKey != null) {
                                                        try {
                                                            val uri = Uri.withAppendedPath(
                                                                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                                                lead.contactLookupKey
                                                            )
                                                            uriHandler.openUri(uri.toString())
                                                        } catch (e: Exception) {
                                                            Toast.makeText(
                                                                context,
                                                                "Could not open contact",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    } else if (lead.contact == ContactTypeEnum.SOCIAL.getField() && lead.instagramUrl != null && lead.instagramUrl!!.isNotBlank()) {
                                                        uriHandler.openUri(lead.instagramUrl!!)
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "No contact found",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            })
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}


fun getSentPingForLead(
    leadId: Long,
    pingId: Long,
    sentPingsByLeadMap: Map<Long, List<SentPing>>
): SentPing? {
    val leadPings: List<SentPing> = sentPingsByLeadMap[leadId] ?: return null
    return leadPings.find { it.pingId == pingId }
}