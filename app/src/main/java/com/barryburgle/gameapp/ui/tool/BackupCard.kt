package com.barryburgle.gameapp.ui.tool

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.tool.utils.RowTitle
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting
import com.barryburgle.gameapp.ui.utilities.setting.IconButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun BackupCard(
    cardTitle: String,
    cardSubtitle: String,
    state: ToolsState,
    modifier: Modifier,
    csvFindService: CSVFindService,
    onEvent: (ToolEvent) -> Unit
) {
    var icon: ImageVector? = Icons.Default.Backup
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        val textFieldHeight = 55.dp
        val textFieldColumnWidth = 230.dp
        val localContext = LocalContext.current.applicationContext
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LargeTitleText(cardTitle)
                        IconShadowButton(
                            onClick = {
                                DataExchangeService.backup(state)
                                Toast.makeText(
                                    localContext,
                                    "Successfully backed up all tables",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            imageVector = icon!!,
                            contentDescription = cardTitle,
                            title = cardTitle + " all",
                            color = MaterialTheme.colorScheme.primaryContainer,
                            iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LittleBodyText(cardSubtitle)
                        RowTitle(
                            "Backup folder:", "", textFieldColumnWidth
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(textFieldHeight),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = state.backupFolder,
                                onValueChange = { onEvent(ToolEvent.SetBackupFolder(it)) },
                                placeholder = { LittleBodyText(text = "Insert here the backup folder") },
                                shape = MaterialTheme.shapes.large,
                                modifier = Modifier.height(textFieldHeight)
                            )
                        }
                        LittleBodyText("The backup folder will be created and managed directly under the export folder: /emulated/0/storage/${state.exportFolder}/${state.backupFolder}")
                        CountSetting(
                            text = "Keep last ${state.lastBackup} backups",
                            count = state.lastBackup,
                            description = "The app will keep only the last ${state.lastBackup} backups in the backup folder for all the backed up tables (min 1 to max 10)",
                            onEvent = onEvent as (GenericEvent) -> Unit,
                            saveEvent = ToolEvent::SetLastBackup
                        )
                        IconButtonSetting(text = "Import all backups",
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "Import all",
                            onClick = {
                                DataExchangeService.importAll(
                                    state,
                                    true,
                                    csvFindService,
                                    localContext,
                                    onEvent
                                )
                            })
                        // TODO: if no files found toast
                        SwitchSetting(
                            "Backup after each save", state.backupActive
                        ) {
                            onEvent(ToolEvent.SwitchBackupActive)
                        }
                        SwitchSetting(
                            "Backup before update", state.backupBeforeUpdate
                        ) {
                            onEvent(ToolEvent.SwitchBackupBeforeUpdate)
                        }
                    }
                }
            }
        }
    }
}