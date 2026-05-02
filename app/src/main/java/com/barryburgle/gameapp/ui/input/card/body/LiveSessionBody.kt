package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.AbstractSessionService
import com.barryburgle.gameapp.service.FormatService
import com.barryburgle.gameapp.ui.input.InputCounter
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun LiveSessionBody(
    sessionTime: Long,
    descriptionFontSize: TextUnit,
    perfFontSize: TextUnit,
    onEvent: (GameEvent) -> Unit,
    abstractSession: AbstractSession,
    liveSessionLeads: Int
) {
    var setsCount = abstractSession.sets + liveSessionLeads
    var convosCount = abstractSession.convos + liveSessionLeads
    var contactsCount = abstractSession.contacts + liveSessionLeads
    LittleBodyText("Live session:")
    Spacer(modifier = Modifier.height(7.dp))
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetSetsLive(abstractSession, setsCount - 1))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                InputCounter(
                    count = setsCount,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
                Image(
                    painter = painterResource(R.drawable.set_action),
                    contentDescription = "sets",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(30.dp)
                )
            }
            LittleBodyText("Sets")
            Spacer(modifier = Modifier.height(4.dp))
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetSetsLive(abstractSession, setsCount + 1))
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
            )
        }
        // TODO: use counters in SessionDialog as here, withotu remember and without follow count on FE
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetConvosLive(abstractSession, convosCount - 1, false))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                InputCounter(
                    count = convosCount,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
                Image(
                    painter = painterResource(R.drawable.conversation_action),
                    contentDescription = "conversations",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(30.dp)
                )
            }
            LittleBodyText("Conversations")
            Spacer(modifier = Modifier.height(4.dp))
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetConvosLive(abstractSession, convosCount + 1, true))
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetContactsLive(abstractSession, contactsCount - 1, false))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                InputCounter(
                    count = contactsCount,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
                Image(
                    painter = painterResource(R.drawable.contact_action),
                    contentDescription = "contacts",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(30.dp)
                )
            }
            LittleBodyText("Contacts")
            Spacer(modifier = Modifier.height(4.dp))
            IconShadowButton(
                onClick = {
                    onEvent(GameEvent.SetContactsLive(abstractSession, contactsCount + 1, true))
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
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
            quantity = FormatService.getPerc(
                AbstractSessionService.computeConvoRatio(
                    convosCount,
                    setsCount
                )
            ),
            quantityFontSize = perfFontSize,
            description = "Conversation\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = FormatService.getPerc(
                AbstractSessionService.computeContactRatio(
                    contactsCount,
                    setsCount
                )
            ),
            quantityFontSize = perfFontSize,
            description = "Contact\nRatio",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${
                AbstractSessionService.computeIndex(
                    setsCount,
                    convosCount,
                    contactsCount,
                    sessionTime
                )
            }",
            quantityFontSize = perfFontSize,
            description = "Index",
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = "${
                AbstractSessionService.computeApproachTime(
                    sessionTime,
                    setsCount
                )
            }",
            quantityFontSize = perfFontSize,
            description = "Minutes\nper set",
            descriptionFontSize = descriptionFontSize
        )
    }
}