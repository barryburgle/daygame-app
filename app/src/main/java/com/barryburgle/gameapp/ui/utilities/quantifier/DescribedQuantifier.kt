package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescribedQuantifier(
    quantity: String?,
    quantityFontSize: TextUnit,
    description: String,
    descriptionFontSize: TextUnit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                var shownQuantity = "No"
                if (!quantity.isNullOrBlank()) {
                    shownQuantity = quantity
                }
                Text(
                    text = shownQuantity, fontSize = quantityFontSize
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Text(
                        text = description,
                        fontSize = descriptionFontSize,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}