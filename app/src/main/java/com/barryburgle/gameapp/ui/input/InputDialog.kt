package com.barryburgle.gameapp.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.ui.input.state.InputState

@Composable
fun AddInputDialog(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AbstractSessionEvent.HideDialog)
        },
        title = {
            Text(text = "Add session")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = state.date,
                    onValueChange = { onEvent(AbstractSessionEvent.SetDate(it)) },
                    placeholder = { Text(text = "yyyy-MM-dd date") })
                TextField(value = state.startHour,
                    onValueChange = { onEvent(AbstractSessionEvent.SetStartHour(it)) },
                    placeholder = { Text(text = "hh:mm Start Hour") })
                TextField(value = state.endHour,
                    onValueChange = { onEvent(AbstractSessionEvent.SetEndHour(it)) },
                    placeholder = { Text(text = "hh:mm End Hour") })
                TextField(value = state.sets,
                    onValueChange = { onEvent(AbstractSessionEvent.SetSets(it)) },
                    placeholder = { Text(text = "Sets") })
                TextField(value = state.convos,
                    onValueChange = { onEvent(AbstractSessionEvent.SetConvos(it)) },
                    placeholder = { Text(text = "Convos") })
                TextField(value = state.contacts,
                    onValueChange = { onEvent(AbstractSessionEvent.SetContacts(it)) },
                    placeholder = { Text(text = "Contacts") })
                TextField(value = state.stickingPoints,
                    onValueChange = { onEvent(AbstractSessionEvent.SetStickingPoints(it)) },
                    placeholder = { Text(text = "Sticking Points") })
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(AbstractSessionEvent.SaveAbstractSession)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}