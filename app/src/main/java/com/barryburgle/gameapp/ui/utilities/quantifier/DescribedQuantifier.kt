package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
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
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
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
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        AnimatedContent(
                            targetState = shownQuantity,
                            transitionSpec = {
                                val prevInt = initialState.toIntOrNull() ?: 0
                                val currInt = targetState.toIntOrNull() ?: 0
                                if (currInt >= prevInt) {
                                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                                            (slideOutVertically { height -> -height } + fadeOut())
                                } else {
                                    (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                            (slideOutVertically { height -> height } + fadeOut())
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                            label = "DescribedQuantifierSlide"
                        ) { targetVal ->
                            Text(
                                text = targetVal,
                                fontSize = quantityFontSize,
                                fontWeight = FontWeight.Black
                            )
                        }

                        if (drawableIcon != null) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(drawableIcon),
                                    contentDescription = description,
                                    modifier = Modifier.size(16.dp),
                                    contentScale = ContentScale.Fit,
                                    colorFilter = ColorFilter.tint(
                                        color ?: MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
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