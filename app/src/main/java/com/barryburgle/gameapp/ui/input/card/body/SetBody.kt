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
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedIcon
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun SetBody(
    set: SingleSet,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit
) {
    LittleBodyText("Set recap:")
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DescribedIcon(
            SetSortType.CONVERSATION.getField(),
            SetSortType.NO_CONVERSATION.getField(),
            descriptionFontSize,
            R.drawable.chat_w,
            set.conversation
        )
        DescribedIcon(
            SetSortType.CONTACT.getField(),
            SetSortType.NO_CONTACT.getField(),
            descriptionFontSize,
            R.drawable.contact_w,
            set.contact
        )
        DescribedIcon(
            SetSortType.INSTANT_DATE.getField(),
            SetSortType.NO_INSTANT_DATE.getField(),
            descriptionFontSize,
            R.drawable.idate_w,
            set.instantDate
        )
        DescribedIcon(
            SetSortType.RECORDED.getField(),
            SetSortType.NOT_RECORDED.getField(),
            descriptionFontSize,
            R.drawable.microphone_w,
            set.recorded
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
            quantity = "${set.location}",
            quantityFontSize = perfFontSize,
            description = "Location",
            descriptionFontSize = descriptionFontSize
        )
        TweetLinkButton(set.tweetUrl)
    }
}