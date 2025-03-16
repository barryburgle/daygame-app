package com.barryburgle.gameapp.ui.date

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.PinDrop
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.leadName
import com.barryburgle.gameapp.ui.theme.AlertHigh
import com.barryburgle.gameapp.ui.theme.AlertLow
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun DateCard(
    date: Date,
    lead: Lead,
    onEvent: (DateEvent) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp)
                        ) {
                            Text(
                                text = "${FormatService.getDate(date.date!!)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        imageVector = DateType.getIcon(date.dateType),
                                        contentDescription = date.dateType,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .height(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = "${date.dateType.replaceFirstChar { it.uppercase() }} Date",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
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
                                        // TODO: pass fields location, date type and tweet url to dialog before updating
                                        // TODO: make date flags updatable
                                        // TODO: test updateing a date and changing all vars about that
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                ) {
                                    // TODO: integrate in the following subtitle minutes
                                    var subtitle =
                                        "${
                                            FormatService.parseDate(date.date!!).dayOfWeek.toString()
                                                .lowercase()
                                                .replaceFirstChar {
                                                    if (it.isLowerCase()) it.titlecase(
                                                        Locale.getDefault()
                                                    ) else it.toString()
                                                }
                                        } ${
                                            FormatService.getTime(
                                                date.startHour!!
                                            )
                                        } - ${
                                            FormatService.getTime(
                                                date.endHour!!
                                            )
                                        } : ${date.dateTime} minutes"
                                    Row {
                                        Text(
                                            // TODO: convert date to integer inside week and then day of week
                                            // TODO: compute minutes before filling date list
                                            text = subtitle,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.PinDrop,
                                            contentDescription = "Location",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .height(18.dp)
                                        )
                                        Text(
                                            text = date.location!!,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Euro,
                                            contentDescription = "Cost",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .height(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = date.cost.toString(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.SpaceAround,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                // TODO: call edit lead dialog
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
                                            }
                                    ) {
                                        // TODO: only once date, sets and sessions will be displayed in the same view (after refactor) allow lead edit from date card
                                        leadName(
                                            lead = lead,
                                            backgroundColor = MaterialTheme.colorScheme.background,
                                            outputShow = false,
                                            cardShow = true
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(0.8f)
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
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(7.dp)
                                    ) {
                                        var largeDateNumberText = date.dateNumber.toString()
                                        var largeDateNumberTextStyle: TextStyle =
                                            MaterialTheme.typography.titleLarge
                                        if (date.dateNumber != 0) {
                                            Text(
                                                text = "Date:",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                        } else {
                                            largeDateNumberText = "iDate"
                                            largeDateNumberTextStyle =
                                                MaterialTheme.typography.titleMedium
                                        }
                                        Text(
                                            text = largeDateNumberText,
                                            style = largeDateNumberTextStyle
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    describedIcon("Pull", R.drawable.pull_w, date.pull)
                                    describedIcon("Bounce", R.drawable.bounce_w, date.bounce)
                                    describedIcon("Kiss", R.drawable.kiss_w, date.kiss)
                                    describedIcon("Lay", R.drawable.bed_w, date.lay)
                                    describedIcon("Record", R.drawable.microphone_w, date.recorded)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun describedIcon(flagDescription: String, @DrawableRes icon: Int, happened: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var color: Color = AlertLow
        if (!happened) {
            color = AlertHigh
        }
        Image(
            painter = painterResource(icon),
            contentDescription = flagDescription,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(20.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Text(
            text = flagDescription,
            style = MaterialTheme.typography.bodySmall
        )
    }
}