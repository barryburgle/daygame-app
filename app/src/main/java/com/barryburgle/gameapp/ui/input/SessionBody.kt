package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier

@Composable
fun SessionBody(
    abstractSession: AbstractSession,
    countFontSize: TextUnit,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit
) {
    Text(
        text = "Session stats:",
        style = MaterialTheme.typography.bodySmall
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DescribedQuantifier(
            quantity = "${abstractSession.sets}",
            quantityFontSize = countFontSize,
            description = "Sets",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${abstractSession.convos}",
            quantityFontSize = countFontSize,
            description = "Conversations",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${abstractSession.contacts}",
            quantityFontSize = countFontSize,
            description = "Contacts",
            descriptionFontSize = descriptionFontSize
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
            quantity = "${FormatService.getPerc(abstractSession.convoRatio)}",
            quantityFontSize = perfFontSize,
            description = "Conversation\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${FormatService.getPerc(abstractSession.contactRatio)}",
            quantityFontSize = perfFontSize,
            description = "Contact\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${abstractSession.index}",
            quantityFontSize = perfFontSize,
            description = "Index",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${abstractSession.approachTime}",
            quantityFontSize = perfFontSize,
            description = "Minutes\nper set",
            descriptionFontSize = descriptionFontSize
        )
    }
}