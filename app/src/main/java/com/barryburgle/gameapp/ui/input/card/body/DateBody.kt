package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedIcon
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun DateBody(
    date: Date,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit
) {
    LittleBodyText("Date recap:")
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
            quantity = "${date.location}",
            quantityFontSize = perfFontSize,
            description = "Location",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${date.cost} €",
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
        TweetLinkButton(date.tweetUrl)
    }
}