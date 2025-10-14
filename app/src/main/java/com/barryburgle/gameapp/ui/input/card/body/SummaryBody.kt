package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
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
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(0.2f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(0.2f)
                            ),
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .width(75.dp)
                    .height(75.dp),
                contentAlignment = Alignment.Center
            ) {
                DescribedQuantifier(
                    quantity = "${sets}",
                    quantityFontSize = countFontSize,
                    description = "Sets",
                    descriptionFontSize = descriptionFontSize
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(0.2f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(0.2f)
                            ),
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .width(75.dp)
                    .height(75.dp),
                contentAlignment = Alignment.Center
            ) {
                DescribedQuantifier(
                    quantity = "${contacts}",
                    quantityFontSize = countFontSize,
                    description = "Contacts",
                    descriptionFontSize = descriptionFontSize
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(0.2f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(0.2f)
                            ),
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .width(75.dp)
                    .height(75.dp),
                contentAlignment = Alignment.Center
            ) {
                DescribedQuantifier(
                    quantity = "${dates}",
                    quantityFontSize = countFontSize,
                    description = "Dates",
                    descriptionFontSize = descriptionFontSize
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    LittleBodyText(timeSpentDescription + " in the " + period.lowercase() + ".")
}