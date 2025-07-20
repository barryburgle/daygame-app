package com.barryburgle.gameapp.ui.input.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.ui.input.InputCounter
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun SessionCountComponent(
    state: InputState,
    onEvent: (GameEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var setsCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.sets
        var convosCountStart = if (state.isAddingSession) 0 else state.editAbstractSession?.convos
        var contactsCountStart =
            if (state.isAddingSession) 0 else state.editAbstractSession?.contacts
        var setsCount by remember {
            mutableStateOf(if (setsCountStart == null) 0 else setsCountStart)
        }
        var convosCount by remember {
            mutableStateOf(if (convosCountStart == null) 0 else convosCountStart)
        }
        var contactsCount by remember {
            mutableStateOf(if (contactsCountStart == null) 0 else contactsCountStart)
        }
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
                    if (state.followCount) {
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
                    if (state.followCount) {
                        setsCount++
                        convosCount++
                    }
                },
                imageVector = Icons.Default.Add,
                contentDescription = "More"
            )
        }
    }
}