package com.barryburgle.gameapp.ui.date

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.DateEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.DateType
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.describedQuantifier
import com.barryburgle.gameapp.ui.input.leadName
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun DateCard(
    date: Date,
    lead: Lead,
    onEvent: (DateEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    val uriHandler = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
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
                                        // TODO: import date flags values from the date
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
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
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
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                describedIcon(
                                    DateSortType.PULL.getField(),
                                    DateSortType.NOT_PULL.getField(),
                                    descriptionFontSize,
                                    R.drawable.pull_w,
                                    date.pull
                                )
                                describedIcon(
                                    DateSortType.BOUNCE.getField(),
                                    DateSortType.NOT_BOUNCE.getField(),
                                    descriptionFontSize,
                                    R.drawable.bounce_w,
                                    date.bounce
                                )
                                describedIcon(
                                    DateSortType.KISS.getField(),
                                    DateSortType.NOT_KISS.getField(),
                                    descriptionFontSize,
                                    R.drawable.kiss_w,
                                    date.kiss
                                )
                                describedIcon(
                                    DateSortType.LAY.getField(),
                                    DateSortType.NOT_LAY.getField(),
                                    descriptionFontSize,
                                    R.drawable.bed_w,
                                    date.lay
                                )
                                describedIcon(
                                    DateSortType.RECORD.getField(),
                                    DateSortType.NOT_RECORD.getField(),
                                    descriptionFontSize,
                                    R.drawable.microphone_w,
                                    date.recorded
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
                                    quantity = "${date.location!!}",
                                    quantityFontSize = perfFontSize,
                                    description = "Location",
                                    descriptionFontSize = descriptionFontSize
                                )
                                describedQuantifier(
                                    quantity = "${date.cost!!} €",
                                    quantityFontSize = perfFontSize,
                                    description = "Cost",
                                    descriptionFontSize = descriptionFontSize
                                )
                                var dateNumberSuffix = "th"
                                var dateNumberCount = date.dateNumber.toString()
                                if (date.dateNumber == 0) {
                                    dateNumberCount = "Instant"
                                    dateNumberSuffix = ""
                                } else if (date.dateNumber == 1) {
                                    dateNumberSuffix = "st"
                                } else if (date.dateNumber == 2) {
                                    dateNumberSuffix = "nd"
                                } else if (date.dateNumber == 3) {
                                    dateNumberSuffix = "rd"
                                }
                                describedQuantifier(
                                    quantity = "${dateNumberCount}${dateNumberSuffix}",
                                    quantityFontSize = perfFontSize,
                                    description = "Date",
                                    descriptionFontSize = descriptionFontSize
                                )
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(30.dp)
                                            )
                                            .size(30.dp)
                                    ) {
                                        IconButton(onClick = {
                                            if (date.tweetUrl != null && date.tweetUrl!!.isNotBlank()) {
                                                uriHandler.openUri(date.tweetUrl!!)
                                            } else {
                                                Toast.makeText(
                                                    localContext,
                                                    "No tweet url saved",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowOutward,
                                                contentDescription = "Tweet",
                                                tint = MaterialTheme.colorScheme.inversePrimary,
                                                modifier = Modifier
                                                    .height(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        text = "Tweet",
                                        fontSize = descriptionFontSize,
                                        lineHeight = 10.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
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
                                        },
                                        horizontalArrangement = Arrangement.spacedBy(7.dp)
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
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        var stickingPoints = "No sticking points"
                        if (date.stickingPoints != null || !date.stickingPoints!!.isBlank()) {
                            stickingPoints = date.stickingPoints.toString()
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(7.dp)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(stickingPoints))
                                    Toast.makeText(
                                        localContext,
                                        "Sticking points copied",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        ) {
                            Text(
                                text = "Sticking Points:",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(5.dp))
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

@Composable
fun describedIcon(
    trueFlagDescription: String,
    falseFlagDescription: String,
    descriptionFontSize: TextUnit,
    @DrawableRes icon: Int,
    happened: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
    ) {
        var color: Color = MaterialTheme.colorScheme.onPrimary
        var flagDescription = trueFlagDescription
        if (!happened) {
            color = MaterialTheme.colorScheme.secondaryContainer
            flagDescription = falseFlagDescription
        }
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(25.dp)
                )
                .size(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = flagDescription,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(30.dp),
                colorFilter = ColorFilter.tint(color)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = flagDescription,
            fontSize = descriptionFontSize,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}