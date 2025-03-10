package com.barryburgle.gameapp.ui.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.leadName

@ExperimentalMaterial3Api
@Composable
fun DateCard(
    date: Date,
    lead: Lead,
    onEvent: (DateEvent) -> Unit,
    modifier: Modifier = Modifier
) {
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
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // TODO: date entity should possess also date type enum field to track wine/drink/coffee/walk/restaurant/experience types
                            Icon(
                                imageVector = Icons.Default.WineBar,
                                contentDescription = "Date",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            val title =
                                if (date.dateNumber == 0L) "iDate" else "Date ${date.dateNumber}"
                            Text(
                                text = "$title with ${lead.name}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier.width(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                    onEvent(
                                        DateEvent.DeleteDate(
                                            date
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Date",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(30.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                    onEvent(
                                        DateEvent.EditDate(
                                            date
                                        )
                                    )
                                    onEvent(
                                        DateEvent.ShowDialog(false, true)
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Date",
                                        tint = MaterialTheme.colorScheme.inversePrimary,
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    val weekday =
                        "Monday"/*DayOfWeek.of(date.date/*)*/.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() */
                    val subtitle =
                        "On Monday ${date.date} from ${date.startTime} to ${date.endTime}"/*"On $weekday ${
    FormatService.getDate(
        date.date!!
    )
} from ${
    if (date.startTime.isNullOrBlank()) "00:00" else FormatService.getTime(
        date.startTime!!
    )
} to ${
    if (date.endTime.isNullOrBlank()) "00:00" else FormatService.getTime(
        date.endTime!!
    )
} : [compute minutes] minutes"*/
                    Text(
                        // TODO: convert date to integer inside week and then day of week
                        // TODO: compute minutes before filling date list
                        text = subtitle,
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
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(7.dp)
                            ) {
                                Text(
                                    text = "Lead:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row(
                                    modifier = Modifier.clickable {
                                        onEvent(
                                            DateEvent.EditLead(
                                                lead,
                                                true
                                            )
                                        )
                                        /*onEvent(
                                            DateEvent.ShowLeadDialog(
                                                false,
                                                false,
                                                true
                                            )
                                        )*/
                                    },
                                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                                ) {
                                    leadName(
                                        lead = lead,
                                        backgroundColor = MaterialTheme.colorScheme.background,
                                        outputShow = false,
                                        cardShow = true
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(7.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .background(
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(7.dp)
                                    ) {
                                        Text(
                                            text = "Sticking Points:",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        val stickingPoints =
                                            if (date.stickingPoints!!.isBlank()) "No sticking points" else date.stickingPoints
                                        if (stickingPoints != null) {
                                            Text(
                                                text = stickingPoints,
                                                style = MaterialTheme.typography.bodyMedium
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
    }
}