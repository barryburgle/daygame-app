package com.barryburgle.gameapp.ui.output

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.model.enums.HeatmapEntityEnum
import com.barryburgle.gameapp.service.FormatService
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

data class ContributionEntry(
    val date: LocalDate,
    val count: Float,
    val desc: String
)

@Composable
fun HeatmapCalendar(
    modifier: Modifier = Modifier,
    entries: List<ContributionEntry>,
    weeksToShow: Int = 26,
    textColor: Color,
    cellColor: Color,
    emptyColor: Color,
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val endDate = remember { LocalDate.now() }
    val startDate =
        remember { endDate.minusWeeks(weeksToShow.toLong()).with(java.time.DayOfWeek.MONDAY) }

    val entryMap = remember(entries) { entries.associateBy { it.date } }
    val avgCount = remember(entries) {
        if (entries.isEmpty()) 0f
        else entries.map { it.count }.average().toFloat()
    }
    val weeks = remember(startDate, endDate) {
        val daysCount = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        (0..daysCount)
            .map { startDate.plusDays(it.toLong()) }
            .chunked(7)
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(weeks) {
        scope.launch {
            if (weeks.isNotEmpty()) {
                listState.scrollToItem(weeks.size - 1)
            }
        }
    }
    Row(modifier = modifier.padding(vertical = 8.dp)) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            itemsIndexed(weeks) { index, week ->
                Column {
                    val firstDayOfWeek = week.first()
                    Box(modifier = Modifier.height(20.dp)) {
                        if (index == 0 || firstDayOfWeek.dayOfMonth <= 7) {
                            Text(
                                text = firstDayOfWeek.month.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                maxLines = 1,
                                modifier = Modifier.offset(x = 24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        week.forEach { date ->
                            val entry = entryMap[date]
                            val count = entry?.count ?: 0.0f
                            val desc = entry?.desc ?: ""
                            ContributionCellWithTooltip(
                                date = date,
                                count = count,
                                desc = desc,
                                avgCount = avgCount,
                                textColor = textColor,
                                cellColor = cellColor,
                                emptyColor = emptyColor.copy(alpha = 0.2f),
                                isSelected = selectedDate == date,
                                onClick = { selectedDate = date }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContributionCellWithTooltip(
    date: LocalDate,
    count: Float,
    desc: String,
    avgCount: Float,
    textColor: Color,
    cellColor: Color,
    emptyColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    val scope = rememberCoroutineScope()
    val baseColor = when {
        count == 0.0f -> emptyColor
        else -> {
            var ratio = count.toFloat() / (2 * avgCount)
            if (ratio > 1f) {
                ratio = 1f
            }
            val intensity = ratio.coerceIn(0f, 1f)
            cellColor.copy(alpha = intensity)
        }
    }
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    color = textColor,
                    text = date.dayOfWeek.name.lowercase()
                        .replaceFirstChar { it.uppercase() }.take(3) + ". " + FormatService.getDate(
                        date.toString() + "T00:00Z"
                    ) + desc
                )
            }
        },
        state = tooltipState
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .then(
                    if (isSelected) Modifier.border(1.5.dp, textColor, RoundedCornerShape(2.dp))
                    else Modifier
                )
                .background(baseColor, RoundedCornerShape(2.dp))
                .clickable {
                    onClick()
                    scope.launch { tooltipState.show() }
                }
        )
    }
}