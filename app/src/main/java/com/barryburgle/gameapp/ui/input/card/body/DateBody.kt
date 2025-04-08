package com.barryburgle.gameapp.ui.input.card.body

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedIcon
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier

@Composable
fun DateBody(
    date: Date,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit
) {
    val localContext = LocalContext.current.applicationContext
    val uriHandler = LocalUriHandler.current
    Text(
        text = "Date recap:",
        style = MaterialTheme.typography.bodySmall
    )
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DescribedIcon(
            DateSortType.PULL.getField(),
            DateSortType.NOT_PULL.getField(),
            descriptionFontSize,
            R.drawable.pull_w,
            date.pull
        )
        DescribedIcon(
            DateSortType.BOUNCE.getField(),
            DateSortType.NOT_BOUNCE.getField(),
            descriptionFontSize,
            R.drawable.bounce_w,
            date.bounce
        )
        DescribedIcon(
            DateSortType.KISS.getField(),
            DateSortType.NOT_KISS.getField(),
            descriptionFontSize,
            R.drawable.kiss_w,
            date.kiss
        )
        DescribedIcon(
            DateSortType.LAY.getField(),
            DateSortType.NOT_LAY.getField(),
            descriptionFontSize,
            R.drawable.bed_w,
            date.lay
        )
        DescribedIcon(
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
        DescribedQuantifier(
            quantity = "${date.location!!}",
            quantityFontSize = perfFontSize,
            description = "Location",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
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
        DescribedQuantifier(
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
}