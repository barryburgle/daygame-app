package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.date.DatePhase
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.input.dialog.text.WavyTextComponent
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun DatePhaseEditDialog(
    dialogTitle: String, onEvent: (GameEvent) -> Unit, datePhase: DatePhase? = null
) {
    val context = LocalContext.current
    val localContext = context.applicationContext
    var showDeleteDatePhaseDialog by remember { mutableStateOf(false) }
    if (showDeleteDatePhaseDialog && datePhase != null) {
        DeleteConfirmationDialog(
            "date phase",
            "Do you want to delete this date phase?",
            onConfirmRequest = {
                onEvent(GameEvent.DeleteDatePhase(datePhase!!.id))
                // TODO: here you should switch the justSaved flag to trigger a backup of date models and phases and so on
                onEvent(GameEvent.SwitchJustSaved)
                onEvent(GameEvent.HideDatePhaseDialog)
                showDeleteDatePhaseDialog = false
            },
            onDismissRequest = {
                showDeleteDatePhaseDialog = false
            },
        )
    }

    var title by remember(datePhase) { mutableStateOf(datePhase?.title ?: "") }
    var description by remember(datePhase) { mutableStateOf(datePhase?.description ?: "") }
    var duration by remember(datePhase) { mutableStateOf(datePhase?.duration ?: 15L) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(GameEvent.HideDatePhaseDialog)
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LargeTitleText("${dialogTitle} date phase")
                // TODO: we should show here in which date models a specific date phase is used when editing it
                IconShadowButton(
                    onClick = {
                        showDeleteDatePhaseDialog = true
                    },
                    imageVector = Icons.Default.Delete,
                    iconColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = "Delete date phase"
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WavyTextComponent(
                    value = title, placeholder = "Date phase title"
                ) {
                    title = it
                }
                WavyTextComponent(
                    value = description, placeholder = "Date phase description", singleLine = false
                ) {
                    description = it
                }
                // Improperly using CountSetting here because I need a counter with desc & horizontal +/- buttons
                CountSetting(
                    text = "Duration of ${if (datePhase != null) datePhase!!.title else "this"} date phase",
                    description = "This date phase will last, in all the date models set up, ${if (datePhase != null) datePhase!!.duration else "15 (default)"} minutes before the next one starts with a new notification",
                    count = duration.toInt(),
                    countBy = 1,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    saveEvent = { input ->
                        duration = input.toLong()
                        object :
                            GenericEvent {} // Doing this because CountSetting requires a GenericEvent
                        // TODO: create a component similar to CountSetting, to use in not-setting places, that needs no GenericEvent
                    }
                )
            }
        },
        confirmButton = {
            ConfirmButton {
                if (title.isBlank()) {
                    Toast.makeText(localContext, "Please enter a title", Toast.LENGTH_SHORT).show()
                } else {
                    val datePhaseToSave = DatePhase(
                        title = title,
                        description = description,
                        duration = duration
                    )
                    if (datePhase != null) {
                        datePhaseToSave.id = datePhase.id
                    }
                    onEvent(
                        GameEvent.SaveDatePhase(datePhaseToSave)
                    )
                    onEvent(GameEvent.SwitchJustSaved)
                    onEvent(GameEvent.HideDatePhaseDialog)
                    Toast.makeText(localContext, "Date phase saved", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.HideDatePhaseDialog)
            }
        })
}