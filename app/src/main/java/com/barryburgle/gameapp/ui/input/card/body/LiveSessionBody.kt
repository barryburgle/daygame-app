package com.barryburgle.gameapp.ui.input.card.body

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
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
    followCount: Boolean
) {
    var setsCount by remember {
        mutableStateOf(0)
    }
    var convosCount by remember {
        mutableStateOf(0)
    }
    var contactsCount by remember {
        mutableStateOf(0)
    }
    LittleBodyText("Live session:")
    Spacer(modifier = Modifier.height(7.dp))
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LittleBodyText("Sets")
            IconShadowButton(
                onClick = {
                    setsCount--
                    onEvent(GameEvent.SetSets(setsCount.toString()))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            InputCounter(
                count = setsCount,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )
            IconShadowButton(
                onClick = {
                    setsCount++
                    onEvent(GameEvent.SetSets(setsCount.toString()))
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LittleBodyText("Conversations")
            IconShadowButton(
                onClick = {
                    convosCount--
                    onEvent(GameEvent.SetConvos(convosCount.toString()))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            InputCounter(
                count = convosCount,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )
            IconShadowButton(
                onClick = {
                    convosCount++
                    onEvent(GameEvent.SetConvos(convosCount.toString()))
                    if (followCount) {
                        setsCount++
                    }
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LittleBodyText("Contacts")
            IconShadowButton(
                onClick = {
                    contactsCount--
                    onEvent(GameEvent.SetContacts(contactsCount.toString()))
                },
                imageVector = Icons.Default.Remove,
                contentDescription = "Less"
            )
            InputCounter(
                count = contactsCount,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )
            IconShadowButton(
                onClick = {
                    contactsCount++
                    onEvent(GameEvent.SetContacts(contactsCount.toString()))
                    if (followCount) {
                        setsCount++
                        convosCount++
                    }
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