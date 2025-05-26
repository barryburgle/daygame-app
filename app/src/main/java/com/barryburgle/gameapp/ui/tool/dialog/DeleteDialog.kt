package com.barryburgle.gameapp.ui.tool.dialog


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.tool.utils.Switch
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun DeleteDialog(
    state: ToolsState,
    onEvent: (ToolEvent) -> Unit,
    description: String,
    csvFindService: CSVFindService,
    modifier: Modifier = Modifier.width(260.dp)
) {
    val localContext = LocalContext.current.applicationContext
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(ToolEvent.SwitchIsCleaning)
    }, title = {
        LargeTitleText(text = description)
    }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DialogConstant.ADD_LEAD_COLUMN_WIDTH),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LittleBodyText(getDeleteDescription(state))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    deleteTableSwitch(
                        "Sessions",
                        state.deleteSessions,
                        onEvent,
                        ToolEvent.SwitchDeleteSessions
                    )
                    deleteTableSwitch(
                        "Leads",
                        state.deleteLeads,
                        onEvent,
                        ToolEvent.SwitchDeleteLeads
                    )
                    deleteTableSwitch(
                        "Dates",
                        state.deleteDates,
                        onEvent,
                        ToolEvent.SwitchDeleteDates
                    )
                    deleteTableSwitch(
                        "Sets",
                        state.deleteSets,
                        onEvent,
                        ToolEvent.SwitchDeleteSets
                    )
                }
            }
            OutlinedTextField(
                value = state.deleteConfirmationPrompt,
                onValueChange = { onEvent(ToolEvent.SetDeleteConfirmationPrompt(it)) },
                placeholder = {
                    LittleBodyText("Type here \"delete\" to confirm")
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.height(50.dp)
            )
        }
    }, confirmButton = {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.width(250.dp), horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    onEvent(ToolEvent.SwitchIsCleaning)
                }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    if (state.deleteConfirmationPrompt.isNotBlank() && state.deleteConfirmationPrompt.equals(
                            "delete"
                        )
                    ) {
                        var deletionMessage = "Cleaned"
                        if (state.archiveBackupFolder) {
                            csvFindService.archiveBackups(state.exportFolder, state.backupFolder)
                            deletionMessage = deletionMessage + " and archived backups"
                        }
                        if (state.deleteSessions) {
                            onEvent(ToolEvent.DeleteAllSessions)
                        }
                        if (state.deleteLeads) {
                            onEvent(ToolEvent.DeleteAllLeads)
                        }
                        if (state.deleteDates) {
                            onEvent(ToolEvent.DeleteAllDates)
                        }
                        if (state.deleteSets) {
                            onEvent(ToolEvent.DeleteAllSets)
                        }
                        onEvent(ToolEvent.SetDeleteConfirmationPrompt(""))
                        Toast.makeText(localContext, deletionMessage, Toast.LENGTH_SHORT)
                            .show()
                        onEvent(ToolEvent.SwitchIsCleaning)
                    } else {
                        Toast.makeText(localContext, "Misspelled \"delete\"", Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                    Text(text = "Clean")
                }
            }
        }
    })
}

private fun getDeleteDescription(state: ToolsState): String {
    if (!state.deleteSessions && !state.deleteLeads && !state.deleteDates && !state.deleteSets) {
        return "No tables will be deleted, please select at least one option"
    }
    val deleteSessionsDescription =
        if (state.deleteSessions) " ${state.allSessions.size} sessions," else ""
    val deleteLeadsDescription = if (state.deleteLeads) " ${state.allLeads.size} leads," else ""
    val deleteDatesDescription = if (state.deleteDates) " ${state.allDates.size} dates," else ""
    val deleteSetsDescription = if (state.deleteSets) " ${state.allSets.size} sets," else ""
    val deleteDescription =
        "Permanently deleting${deleteSessionsDescription}${deleteLeadsDescription}${deleteDatesDescription}${deleteSetsDescription}"
    return deleteDescription.dropLast(1)
}

@Composable
private fun deleteTableSwitch(
    description: String,
    flag: Boolean,
    onEvent: (ToolEvent) -> Unit,
    toolEvent: ToolEvent
) {
    val optionDescription = if (flag) "Deleting " + description else description
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        SmallTitleText(optionDescription)
        Spacer(modifier = Modifier.width(5.dp))
        Switch(flag) {
            onEvent(toolEvent)
        }
    }
}