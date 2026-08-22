package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun SummaryBody(
    period: String,
    timeSpentDescription: String,
    sets: Int,
    contacts: Int,
    dates: Int,
    countFontSize: TextUnit,
    descriptionFontSize: TextUnit
) {
    LittleBodyText(period + ":")
    Spacer(modifier = Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DescribedQuantifier(
                quantity = "${sets}",
                quantityFontSize = countFontSize,
                description = "Sets",
                descriptionFontSize = descriptionFontSize,
                drawableIcon = R.drawable.set_action
            )
            DescribedQuantifier(
                quantity = "${contacts}",
                quantityFontSize = countFontSize,
                description = "Contacts",
                descriptionFontSize = descriptionFontSize,
                drawableIcon = R.drawable.contact_action
            )
            DescribedQuantifier(
                quantity = "${dates}",
                quantityFontSize = countFontSize,
                description = "Dates",
                descriptionFontSize = descriptionFontSize,
                drawableIcon = R.drawable.favorite
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    LittleBodyText(timeSpentDescription + " in the " + period.lowercase() + ".")
}