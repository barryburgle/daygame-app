package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

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
    val notificationHourDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = notificationHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set notification hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(ToolEvent.SetNotificationTime(it.toString().substring(0, 5)))
        }
    }
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
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToolCountComponent(
                inputTitle = "Average last", modifier = Modifier,
                style = MaterialTheme.typography.titleSmall,
                onEvent = onEvent,
                saveEvent = ToolEvent::SetLastSessionAverageQuantity,
                initialCount = state.lastSessionAverageQuantity
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.width(120.dp),
                ) {
                    Button(
                        onClick = { notificationHourDialogState.show() }) {
                        Text(text = "Set notification hour")
                    }
                }
            }
            // TODO: time has to be validated
        }
    }
}