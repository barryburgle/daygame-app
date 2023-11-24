package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@ExperimentalMaterial3Api
@Composable
fun SettingsCard(
    state: ToolsState,
    modifier: Modifier = Modifier
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        ),
    onEvent: (ToolEvent) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    Shapes.large
                )
                .padding(10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Average last:")
            OutlinedTextField(
                value = state.lastSessionAverageQuantity.toString(),
                onValueChange = { onEvent(ToolEvent.SetLastSessionAverageQuantity(it)) },
                placeholder = { Text(text = "4") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.height(50.dp).width(80.dp)
            )
            // TODO: integer needs to be validated as 0<n<8
            // TODO: instead of text box use the same used in input dialog for integer
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Last session notification time:")
            OutlinedTextField(
                value = state.notificationTime,
                onValueChange = { onEvent(ToolEvent.SetNotificationTime(it)) },
                placeholder = { Text(text = "18:00") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.height(50.dp).width(80.dp)
            )
            // TODO: time has to be validated
            // TODO: instead of text box use the same used in input dialog for time
        }
    }
}