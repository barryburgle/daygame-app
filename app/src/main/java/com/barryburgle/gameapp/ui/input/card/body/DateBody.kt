package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.enums.DateSortType
import com.barryburgle.gameapp.model.enums.DateTypeEnum
import com.barryburgle.gameapp.ui.utilities.animation.AnimatedStaggeredItem
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedIcon
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier

@Composable
fun DateBody(
    date: Date,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedStaggeredItem(index = 0) {
            DescribedIcon(
                DateSortType.PULL.getField(),
                DateSortType.NOT_PULL.getField(),
                descriptionFontSize,
                R.drawable.pull_w,
                date.pull
            )
        }
        AnimatedStaggeredItem(index = 1) {
            DescribedIcon(
                DateSortType.BOUNCE.getField(),
                DateSortType.NOT_BOUNCE.getField(),
                descriptionFontSize,
                R.drawable.bounce_w,
                date.bounce
            )
        }
        AnimatedStaggeredItem(index = 2) {
            DescribedIcon(
                DateSortType.KISS.getField(),
                DateSortType.NOT_KISS.getField(),
                descriptionFontSize,
                R.drawable.kiss_w,
                date.kiss
            )
        }
        AnimatedStaggeredItem(index = 3) {
            DescribedIcon(
                DateSortType.LAY.getField(),
                DateSortType.NOT_LAY.getField(),
                descriptionFontSize,
                R.drawable.bed_w,
                date.lay
            )
        }
        AnimatedStaggeredItem(index = 4) {
            DescribedIcon(
                DateSortType.RECORD.getField(),
                DateSortType.NOT_RECORD.getField(),
                descriptionFontSize,
                R.drawable.microphone_w,
                date.recorded
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AnimatedStaggeredItem(index = 5) {
            DescribedQuantifier(
                quantity = "${date.location}",
                quantityFontSize = perfFontSize,
                description = "Location",
                descriptionFontSize = descriptionFontSize
            )
        }
        AnimatedStaggeredItem(index = 6) {
            DescribedQuantifier(
                quantity = "${date.cost} €",
                quantityFontSize = perfFontSize,
                description = "Cost",
                descriptionFontSize = descriptionFontSize
            )
        }
        AnimatedStaggeredItem(index = 7) {
            DescribedQuantifier(
                quantity = DateTypeEnum.getDateNumber(date.dateNumber, false),
                quantityFontSize = perfFontSize,
                description = "Date",
                descriptionFontSize = descriptionFontSize
            )
        }
        AnimatedStaggeredItem(index = 8) {
            TweetLinkButton(date.tweetUrl)
        }
    }
}