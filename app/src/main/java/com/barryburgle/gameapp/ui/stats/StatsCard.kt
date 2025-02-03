package com.barryburgle.gameapp.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.ui.input.describedQuantifier

@ExperimentalMaterial3Api
@Composable
fun StatsCard(
    modifier: Modifier = Modifier
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        ),
    title: String,
    description: String,
    firstQuantifierQuantity: String,
    firstQuantifierDescription: String,
    secondQuantifierQuantity: String,
    secondQuantifierDescription: String,
    thirdQuantifierQuantity: String,
    thirdQuantifierDescription: String,
    firstPerformanceQuantity: String,
    firstPerformanceDescription: String,
    secondPerformanceQuantity: String,
    secondPerformanceDescription: String,
    thirdPerformanceQuantity: String? = null,
    thirdPerformanceDescription: String? = null,
    fourthPerformanceQuantity: String? = null,
    fourthPerformanceDescription: String? = null
) {
    val countFontSize = 50.sp
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
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
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
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            describedQuantifier(
                                quantity = firstQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = firstQuantifierDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = secondQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = secondQuantifierDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = thirdQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = thirdQuantifierDescription,
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
                            describedQuantifier(
                                quantity = firstPerformanceQuantity,
                                quantityFontSize = perfFontSize,
                                description = firstPerformanceDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            describedQuantifier(
                                quantity = secondPerformanceQuantity,
                                quantityFontSize = perfFontSize,
                                description = secondPerformanceDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            if (thirdPerformanceQuantity != null && thirdPerformanceDescription != null) {
                                describedQuantifier(
                                    quantity = thirdPerformanceQuantity,
                                    quantityFontSize = perfFontSize,
                                    description = thirdPerformanceDescription,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                            if (fourthPerformanceQuantity != null && fourthPerformanceDescription != null) {
                                describedQuantifier(
                                    quantity = fourthPerformanceQuantity,
                                    quantityFontSize = perfFontSize,
                                    description = fourthPerformanceDescription,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}