package com.barryburgle.gameapp.ui.output

import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.theme.AlertHigh
import com.barryburgle.gameapp.ui.theme.AlertLow
import com.barryburgle.gameapp.ui.theme.AlertMid
import com.barryburgle.gameapp.ui.utilities.animation.VerticalProgressBarBrush
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Composable
fun LeadCard(
    lead: Lead, onEditClick: () -> Unit, onLinkClick: () -> Unit, shortCut: Boolean = false
) {
    val cardColor = MaterialTheme.colorScheme.surface
    var displayName = lead.name
    if (displayName.isNotBlank()) {
        var height = 120.dp
        if (shortCut) {
            height = 40.dp
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .shadow(
                    elevation = 10.dp, shape = MaterialTheme.shapes.large
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = cardColor, shape = RoundedCornerShape(5.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val daysDifference = getDaysFromNow(lead)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .background(
                            brush = VerticalProgressBarBrush(
                                getLeadAlertColor(
                                    lead
                                )
                            ), shape = RoundedCornerShape(4.dp)
                        )
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    if (!shortCut) {
                        MediumTitleText(displayName + " " + CountryEnum.getFlagByAlpha3(lead.nationality))
                        Spacer(modifier = Modifier.height(5.dp))
                        if (lead.age != 0L) {
                            LittleBodyText("${lead.age} years old")
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (lead.nationality.isNotBlank()) {
                            LittleBodyText(CountryEnum.getCountryNameByAlpha3(lead.nationality))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (lead.insertTime.isNotEmpty()) {
                            val halfSentence =
                                if (daysDifference == 0L) "today" else "${daysDifference} days ago"
                            LittleBodyText("Met ${halfSentence}")
                        }
                    } else {
                        val shortLeadTitle =
                            displayName.take(6) + "... " + CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.age.toString()
                        LittleBodyText(shortLeadTitle)
                    }
                }
                if (!shortCut) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        IconShadowButton(
                            onClick = {
                                onEditClick()
                            }, imageVector = Icons.Default.Edit, contentDescription = "Edit"
                        )
                        val isNumber = lead.contact == ContactTypeEnum.NUMBER.getField()
                        val iconRes =
                            if (isNumber) R.drawable.whatsapp_w else R.drawable.instagram_w
                        val desc = if (isNumber) "Whatsapp" else "Instagram"
                        var isLinkValid = false
                        if ((isNumber && lead.contactLookupKey != null) || (!isNumber && lead.instagramUrl != null && lead.instagramUrl!!.isNotBlank())) {
                            isLinkValid = true
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        val localContext = LocalContext.current
                        if (isLinkValid) {
                            val uriHandler = LocalUriHandler.current
                            var uri: String? = null
                            IconShadowButton(
                                onClick = {
                                    try {
                                        if (isNumber) {
                                            uri = Uri.withAppendedPath(
                                                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                                lead.contactLookupKey
                                            ).toString()
                                        } else {
                                            uri = lead.instagramUrl!!
                                        }
                                        uriHandler.openUri(uri)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            localContext,
                                            "Could not open ${desc} link",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, drawableIcon = iconRes, contentDescription = "Contact link"
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .clickable(onClick = {
                                        Toast.makeText(
                                            localContext,
                                            "No ${desc} contact linked",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                    .background(
                                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                                        CircleShape
                                    )
                                    .size(48.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = "Contact link",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .height(20.dp)
                                        .scale(1.2f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getLeadAlertColor(lead: Lead): Color {
    val daysDifference = getDaysFromNow(lead)
    // TODO: color and leadName date should not come from insert time but from session date
    if (lead.insertTime.isEmpty()) {
        return AlertHigh
    }
    if (daysDifference > 7) {
        return AlertHigh
    }
    if (daysDifference > 4) {
        return AlertMid
    }
    return AlertLow
}

@Composable
private fun getDaysFromNow(lead: Lead): Long {
    val now = OffsetDateTime.now()
    if (lead.insertTime.isEmpty()) {
        return 0L
    }
    val leadInsertTime = FormatService.parseDate(lead.insertTime.substring(0, 16) + "Z")
    val daysDifference = ChronoUnit.DAYS.between(leadInsertTime, now)
    return daysDifference
}