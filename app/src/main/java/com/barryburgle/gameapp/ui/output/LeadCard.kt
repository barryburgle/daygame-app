package com.barryburgle.gameapp.ui.output

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun LeadCard(
    lead: Lead,
    backgroundColor: Color,
    alertColor: Color? = null,
    outputShow: Boolean,
    cardShow: Boolean,
    onClick: (() -> Unit)? = null
) {
    var displayName = lead.name
    if (displayName.isNotBlank()) {
        if (displayName.length >= 7) {
            displayName = displayName.substring(0, 5) + "... "
        }
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = MaterialTheme.shapes.large
                ),
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (onClick != null) {
                            Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(),
                                onClick = onClick
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(
                        color = backgroundColor, shape = RoundedCornerShape(20.dp)
                    )
                    .width(80.dp)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
                if (outputShow && alertColor != null) {
                    textColor = MaterialTheme.colorScheme.onPrimary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(8.dp)
                            .background(alertColor, shape = RoundedCornerShape(4.dp))
                    ) {}
                    Spacer(Modifier.height(3.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // TODO: create MediumBodyText with variable injectable color
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                if (outputShow || cardShow) {
                    // TODO: create MediumBodyText with variable injectable color
                    val leadAgeDesc = if (lead.age != 0L) "${lead.age} " else ""
                    Text(
                        text = "${leadAgeDesc}${
                            CountryEnum.getFlagByAlpha3(
                                lead.nationality
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(2.dp)
                    ) {
                        val iconRes = if (lead.contact == ContactTypeEnum.NUMBER.getField()) {
                            R.drawable.whatsapp_w
                        } else {
                            R.drawable.instagram_w
                        }
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = "Contact Icon",
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(textColor)
                        )

                        if ((!lead.contactLookupKey.isNullOrBlank() && lead.contact == ContactTypeEnum.NUMBER.getField()) || (!lead.instagramUrl.isNullOrBlank() && lead.contact == ContactTypeEnum.SOCIAL.getField())) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.TopEnd)
                                    .background(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        shape = CircleShape
                                    )
                                    .border(1.dp, backgroundColor, CircleShape)
                            )
                        }
                    }
                    if (!cardShow) {
                        if (lead.insertTime.isNotBlank()) {
                            LittleBodyText(
                                "${lead.insertTime.substring(8, 10)}/${
                                    lead.insertTime.substring(5, 7)
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}