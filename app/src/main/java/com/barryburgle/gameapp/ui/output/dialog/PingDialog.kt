package com.barryburgle.gameapp.ui.output.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.ping.Ping
import com.barryburgle.gameapp.ui.output.card.PingCard
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun PingDialog(
    pings: List<Ping> = emptyList(),
    onEvent: (OutputEvent) -> Unit
) {
    FlowDialog(
        onDismissRequest = { onEvent(OutputEvent.HidePingDialog) },
        onConfirm = { onEvent(OutputEvent.HidePingDialog) },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    LargeTitleText("Pings", true)
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconShadowButton(
                        onClick = { onEvent(OutputEvent.ShowPingEditDialog) },
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add ping"
                    )
                }
            }
        }
    ) { contentPadding ->
        if (pings.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LittleBodyText("Add some pings, they will come handy!", italic = true)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(pings, key = { it.id }) { ping ->
                    PingCard(ping, onEvent)
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}
