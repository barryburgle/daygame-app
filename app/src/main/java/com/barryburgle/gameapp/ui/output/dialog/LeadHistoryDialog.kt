package com.barryburgle.gameapp.ui.output.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.DateTypeEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.lead.LeadFilterService
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.utilities.animation.AnimatedStaggeredItem
import com.barryburgle.gameapp.ui.utilities.animation.VerticalProgressBarBrush
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText
import java.time.Duration

enum class LeadTimelineFlag(val label: String) {
    TALKED("Talked"),
    CLOSED("Closed"),
    INSTANT_DATE("Instant date"),
    RECORDED("Recorded"),
    PULLED("Pulled"),
    BOUNCED("Bounced"),
    KISSED("Kissed"),
    LAID("Laid")
}

sealed class LeadTimelineEvent(
    open val timestamp: String,
    open val description: String,
    open val icon: ImageVector,
    open val flags: List<LeadTimelineFlag> = emptyList()
) {
    data class MetPinpoint(
        override val timestamp: String,
        override val description: String,
        val latitude: Double? = null,
        val longitude: Double? = null
    ) : LeadTimelineEvent(timestamp, description, Icons.Default.LocationOn)

    data class DateEvent(
        override val timestamp: String,
        override val description: String,
        val dateType: String,
        override val flags: List<LeadTimelineFlag> = emptyList()
    ) : LeadTimelineEvent(timestamp, description, DateTypeEnum.getIcon(dateType), flags)

    data class PingEvent(
        override val timestamp: String,
        override val description: String
    ) : LeadTimelineEvent(timestamp, description, Icons.Default.Radar)

    data class SetEvent(
        override val timestamp: String,
        override val description: String,
        override val flags: List<LeadTimelineFlag> = emptyList()
    ) : LeadTimelineEvent(timestamp, description, Icons.Default.ChatBubbleOutline, flags)
}

@Composable
fun LeadHistoryDialog(
    lead: Lead,
    events: List<LeadTimelineEvent>,
    onDismissRequest: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    val maxDialogHeight = (LocalConfiguration.current.screenHeightDp * 0.85f).dp

    FlowDialog(
        modifier = Modifier.heightIn(max = maxDialogHeight),
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LargeTitleText(
                    "${lead.name} ${CountryEnum.getFlagByAlpha3(lead.nationality)}",
                    true
                )
                IconShadowButton(
                    onClick = {
                        clipboardManager.setText(
                            AnnotatedString(buildLeadHistoryShareText(lead, events))
                        )
                        Toast.makeText(
                            localContext, "Lead history copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    },
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share lead history"
                )
            }
        },
        onConfirm = onDismissRequest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            if (events.isEmpty()) {
                WavyPlaceholder("No lead history to show: it's time to ping her!")
            } else {
                LeadTimelineView(events = events)
            }
        }
    }
}


@Composable
private fun LeadTimelineView(events: List<LeadTimelineEvent>) {
    if (events.isEmpty()) {
        return
    }
    val density = LocalDensity.current
    val gapHeight = 40.dp
    val iconBoxSize = 32.dp
    val iconCenter = 16.dp
    val barWidth = 8.dp
    val barStartPadding = 12.dp
    var lastRowHeightPx by remember { mutableStateOf(0) }

    val bottomInset = with(density) {
        (lastRowHeightPx.toFloat() - iconCenter.toPx()).coerceAtLeast(0f).toDp()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (lastRowHeightPx > 0) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(start = barStartPadding)
            ) {
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .fillMaxHeight()
                        .padding(top = iconCenter, bottom = bottomInset)
                        .background(
                            brush = VerticalProgressBarBrush(MaterialTheme.colorScheme.onTertiary),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            events.forEachIndexed { index, event ->
                val prevEvent = events.getOrNull(index - 1)
                val timeGap = prevEvent?.let {
                    formatTimeBetween(it.timestamp, event.timestamp)
                }
                val isLast = index == events.lastIndex

                AnimatedStaggeredItem(
                    index = index,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (index > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(gapHeight),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(44.dp))
                                if (timeGap != null) {
                                    WavyPlaceholder(
                                        text = timeGap,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged {
                                    if (isLast) {
                                        lastRowHeightPx = it.height
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(iconBoxSize)
                                        .shadow(elevation = 4.dp, shape = CircleShape)
                                        .background(
                                            color = MaterialTheme.colorScheme.tertiary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = event.icon,
                                        contentDescription = event.description,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    MediumTitleText(text = event.description)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    LittleBodyText(text = formatDateDisplay(event.timestamp))
                                }

                                if (event.flags.isNotEmpty()) {
                                    OverlappingFlagCircles(
                                        flags = event.flags,
                                        modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LeadTimelineFlag.iconRes(): Int {
    return when (this) {
        LeadTimelineFlag.TALKED -> R.drawable.chat_w
        LeadTimelineFlag.CLOSED -> R.drawable.contact_w
        LeadTimelineFlag.INSTANT_DATE -> R.drawable.idate_w
        LeadTimelineFlag.RECORDED -> R.drawable.microphone_w
        LeadTimelineFlag.PULLED -> R.drawable.pull_w
        LeadTimelineFlag.BOUNCED -> R.drawable.bounce_w
        LeadTimelineFlag.KISSED -> R.drawable.kiss_w
        LeadTimelineFlag.LAID -> R.drawable.bed_w
    }
}

@Composable
private fun OverlappingFlagCircles(
    flags: List<LeadTimelineFlag>,
    modifier: Modifier = Modifier
) {
    if (flags.isEmpty()) {
        return
    }

    val circleSize = 24.dp
    val overlap = 8.dp
    val spacing = circleSize - overlap

    Box(
        modifier = modifier
            .height(circleSize)
            .width(circleSize + spacing * (flags.size - 1).toFloat())
    ) {
        flags.forEachIndexed { index, flag ->
            Box(
                modifier = Modifier
                    .offset(x = spacing * index.toFloat())
                    .size(circleSize)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = flag.iconRes()),
                    contentDescription = flag.label,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun formatTimeBetween(previousTimestamp: String, currentTimestamp: String): String? {
    val current = LeadFilterService.parseTimestamp(currentTimestamp) ?: return null
    val previous = LeadFilterService.parseTimestamp(previousTimestamp) ?: return null
    val duration = Duration.between(previous, current)
    if (duration.isZero || duration.isNegative) {
        return null
    }

    if (duration > Duration.ofDays(1)) {
        val days = duration.toDays()
        return "${formatDurationPart(days, "day")} in between"
    }

    return "same day"
}

private fun formatDurationPart(value: Long, unit: String): String {
    return "$value $unit${if (value == 1L) "" else "s"}"
}

fun buildLeadHistoryShareText(lead: Lead, events: List<LeadTimelineEvent>): String {
    val builder = StringBuilder()
    builder.append("${lead.name} ${CountryEnum.getFlagByAlpha3(lead.nationality)}")
    if (lead.age != 0L) {
        builder.append(" · ${lead.age} years old")
    }
    builder.append("\n\n")
    if (events.isEmpty()) {
        builder.append("No timeline events recorded.")
        return builder.toString()
    }

    events.forEachIndexed { index, event ->
        if (index > 0) {
            val timeGap = formatTimeBetween(events[index - 1].timestamp, event.timestamp)
            if (timeGap != null) {
                builder.append("|\n")
                builder.append("| [$timeGap]\n")
                builder.append("|\n")
            }
        }
        val flags = if (event.flags.isNotEmpty()) {
            " (${event.flags.joinToString(", ") { it.label.lowercase() }})"
        } else ""
        builder.append("• ${formatDateDisplay(event.timestamp)} — ${event.description}$flags\n")
    }
    return builder.toString().trimEnd()
}


fun formatDateDisplay(dateStr: String): String {
    val dateTime = LeadFilterService.parseTimestamp(dateStr) ?: return dateStr
    return "%02d/%02d/%04d %02d:%02d".format(
        dateTime.dayOfMonth,
        dateTime.monthValue,
        dateTime.year,
        dateTime.hour,
        dateTime.minute
    )
}