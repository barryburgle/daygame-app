package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescribedQuantifier(
    quantity: String?,
    icon: Int? = null,
    drawableIcon: Int? = null,
    color: Color? = null,
    quantityFontSize: TextUnit,
    description: String,
    descriptionFontSize: TextUnit
) {
    // TODO: extend to support draawable icons on the right side of sets/convos/contacts
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
                if (icon != null) {
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        DescribedIcon(
                            description,
                            description,
                            10.sp,
                            icon,
                            isBoolean = false,
                            defaultColor = color
                        )
                        if (!quantity.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .offset(x = 0.dp, y = (-2).dp)
                                    .size(20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = quantity,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = androidx.compose.ui.text.TextStyle(
                                        platformStyle = androidx.compose.ui.text.PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        lineHeight = 0.sp
                                    )
                                )
                            }
                        }
                    }
                } else {
                    var shownQuantity = "No"
                    if (!quantity.isNullOrBlank()) {
                        shownQuantity = quantity
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = shownQuantity, fontSize = quantityFontSize)
                        if (drawableIcon != null) {
                            Image(
                                painter = painterResource(drawableIcon),
                                contentDescription = description,
                                modifier = Modifier.height(50.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight()
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
}