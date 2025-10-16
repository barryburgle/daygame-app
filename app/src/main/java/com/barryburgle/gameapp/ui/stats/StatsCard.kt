package com.barryburgle.gameapp.ui.stats

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@ExperimentalMaterial3Api
@Composable
fun StatsCard(
    modifier: Modifier,
    title: String,
    statCardIcon: ImageVector,
    description: String,
    copyReportOnClipboard: Boolean,
    firstQuantifierQuantity: String,
    firstQuantifierDescription: String,
    secondQuantifierQuantity: String,
    secondQuantifierDescription: String,
    thirdQuantifierQuantity: String,
    thirdQuantifierDescription: String,
    fourthQuantifierQuantity: String? = null,
    fourthQuantifierDescription: String? = null,
    fifthQuantifierQuantity: String? = null,
    fifthQuantifierDescription: String? = null,
    firstPerformanceQuantity: String,
    firstPerformanceDescription: String,
    secondPerformanceQuantity: String,
    secondPerformanceDescription: String,
    thirdPerformanceQuantity: String? = null,
    thirdPerformanceDescription: String? = null,
    fourthPerformanceQuantity: String? = null,
    fourthPerformanceDescription: String? = null,
    fifthPerformanceQuantity: String? = null,
    fifthPerformanceDescription: String? = null,
) {
    val countFontSize = 50.sp
    val perfFontSize = 15.sp
    val descriptionFontSize = 10.sp
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
                )
            )
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = statCardIcon,
                                contentDescription = title,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            LargeTitleText(title)
                        }
                        Row(
                            modifier = Modifier
                                .width(60.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconShadowButton(
                                onClick = {
                                    var histogramData = exportStats(
                                        title,
                                        description,
                                        firstQuantifierQuantity,
                                        firstQuantifierDescription,
                                        secondQuantifierQuantity,
                                        secondQuantifierDescription,
                                        thirdQuantifierQuantity,
                                        thirdQuantifierDescription,
                                        fourthQuantifierQuantity,
                                        fourthQuantifierDescription,
                                        fifthQuantifierQuantity,
                                        fifthQuantifierDescription,
                                        firstPerformanceQuantity,
                                        firstPerformanceDescription,
                                        secondPerformanceQuantity,
                                        secondPerformanceDescription,
                                        thirdPerformanceQuantity,
                                        thirdPerformanceDescription,
                                        fourthPerformanceQuantity,
                                        fourthPerformanceDescription,
                                        fifthPerformanceQuantity,
                                        fifthPerformanceDescription
                                    )
                                    if (copyReportOnClipboard) {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                histogramData
                                            )
                                        )
                                        Toast.makeText(
                                            localContext,
                                            "Summary copied",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            histogramData
                                        )
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(
                                        sendIntent,
                                        "Share stats"
                                    )
                                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    localContext.startActivity(shareIntent)
                                },
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Stats"
                            )
                        }
                    }
                    LittleBodyText(description)
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
                            DescribedQuantifier(
                                quantity = firstQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = firstQuantifierDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            DescribedQuantifier(
                                quantity = secondQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = secondQuantifierDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            DescribedQuantifier(
                                quantity = thirdQuantifierQuantity,
                                quantityFontSize = countFontSize,
                                description = thirdQuantifierDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            if (fourthQuantifierQuantity != null && fourthQuantifierDescription != null) {
                                DescribedQuantifier(
                                    quantity = fourthQuantifierQuantity,
                                    quantityFontSize = countFontSize,
                                    description = fourthQuantifierDescription,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                            if (fifthQuantifierQuantity != null && fifthQuantifierDescription != null) {
                                DescribedQuantifier(
                                    quantity = fifthQuantifierQuantity,
                                    quantityFontSize = countFontSize,
                                    description = fifthQuantifierDescription,
                                    descriptionFontSize = descriptionFontSize
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
                            DescribedQuantifier(
                                quantity = firstPerformanceQuantity,
                                quantityFontSize = perfFontSize,
                                description = firstPerformanceDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            DescribedQuantifier(
                                quantity = secondPerformanceQuantity,
                                quantityFontSize = perfFontSize,
                                description = secondPerformanceDescription,
                                descriptionFontSize = descriptionFontSize
                            )
                            if (thirdPerformanceQuantity != null && thirdPerformanceDescription != null) {
                                DescribedQuantifier(
                                    quantity = thirdPerformanceQuantity,
                                    quantityFontSize = perfFontSize,
                                    description = thirdPerformanceDescription,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                            if (fourthPerformanceQuantity != null && fourthPerformanceDescription != null) {
                                DescribedQuantifier(
                                    quantity = fourthPerformanceQuantity,
                                    quantityFontSize = perfFontSize,
                                    description = fourthPerformanceDescription,
                                    descriptionFontSize = descriptionFontSize
                                )
                            }
                            if (fifthPerformanceQuantity != null && fifthPerformanceDescription != null) {
                                DescribedQuantifier(
                                    quantity = fifthPerformanceQuantity,
                                    quantityFontSize = perfFontSize,
                                    description = fifthPerformanceDescription,
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

fun exportStats(
    title: String,
    description: String,
    firstQuantifierQuantity: String,
    firstQuantifierDescription: String,
    secondQuantifierQuantity: String,
    secondQuantifierDescription: String,
    thirdQuantifierQuantity: String,
    thirdQuantifierDescription: String,
    fourthQuantifierQuantity: String? = null,
    fourthQuantifierDescription: String? = null,
    fifthQuantifierQuantity: String? = null,
    fifthQuantifierDescription: String? = null,
    firstPerformanceQuantity: String,
    firstPerformanceDescription: String,
    secondPerformanceQuantity: String,
    secondPerformanceDescription: String,
    thirdPerformanceQuantity: String? = null,
    thirdPerformanceDescription: String? = null,
    fourthPerformanceQuantity: String? = null,
    fourthPerformanceDescription: String? = null,
    fifthPerformanceQuantity: String? = null,
    fifthPerformanceDescription: String? = null
): String {
    var quantifierResult = title + ":\n\n" + description + "\n\n" +
            firstQuantifierDescription + ": " + firstQuantifierQuantity + "\n" +
            secondQuantifierDescription + ": " + secondQuantifierQuantity + "\n" +
            thirdQuantifierDescription + ": " + thirdQuantifierQuantity + "\n"
    quantifierResult = addDescriptionAndQuantifierIfNotNull(
        quantifierResult,
        fourthQuantifierDescription,
        fourthQuantifierQuantity
    )
    quantifierResult = addDescriptionAndQuantifierIfNotNull(
        quantifierResult,
        fifthQuantifierDescription,
        fifthQuantifierQuantity
    )
    quantifierResult += "\n" +
            firstPerformanceDescription.split("\n").joinToString(" ") + ": " + firstPerformanceQuantity + "\n" +
            secondPerformanceDescription.split("\n").joinToString(" ") + ": " + secondPerformanceQuantity + "\n"
    quantifierResult = addDescriptionAndQuantifierIfNotNull(
        quantifierResult,
        thirdPerformanceDescription,
        thirdPerformanceQuantity
    )
    quantifierResult = addDescriptionAndQuantifierIfNotNull(
        quantifierResult,
        fourthPerformanceDescription,
        fourthPerformanceQuantity
    )
    quantifierResult = addDescriptionAndQuantifierIfNotNull(
        quantifierResult,
        fifthPerformanceDescription,
        fifthPerformanceQuantity
    )
    return quantifierResult
}

fun addDescriptionAndQuantifierIfNotNull(
    result: String,
    description: String?,
    quantity: String?
): String {
    if (description != null && quantity != null) {
        return result + description.split("\n").joinToString(" ") + ": " + quantity + "\n"
    }
    return result
}