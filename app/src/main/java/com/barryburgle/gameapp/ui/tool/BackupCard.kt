package com.barryburgle.gameapp.ui.tool

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.tool.utils.RowTitle
import com.barryburgle.gameapp.ui.utilities.button.ShadowButton
import com.barryburgle.gameapp.ui.utilities.setting.ButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

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
                        ShadowButton(
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
                        RowTitle(
                            "Backups:", "", textFieldColumnWidth
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InputCountComponent(
                                inputTitle = "Keep\nlast",
                                modifier = Modifier,
                                style = MaterialTheme.typography.bodySmall,
                                onEvent = onEvent as (GenericEvent) -> Unit,
                                countStart = state.lastBackup,
                                saveEvent = ToolEvent::SetLastBackup
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ), onClick = {
                                        DataExchangeService.importAll(
                                            state,
                                            true,
                                            csvFindService,
                                            localContext,
                                            onEvent
                                        )
                                    }) {
                                        if (icon != null) {
                                            Icon(
                                                imageVector = Icons.Default.CloudDownload,
                                                contentDescription = cardTitle,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.height(25.dp)
                                            )
                                            Spacer(modifier = Modifier.width(7.dp))
                                        }
                                        Text(
                                            text = "Import all", textAlign = TextAlign.Center
                                        )
                                        // TODO: if no files found toast
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    SmallTitleText("After save")
                                    Spacer(modifier = Modifier.width(5.dp))
                                    com.barryburgle.gameapp.ui.tool.utils.Switch(state.backupActive) {
                                        onEvent(ToolEvent.SwitchBackupActive)
                                    }
                                }
                                Spacer(modifier = Modifier.height(7.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    SmallTitleText("Before update")
                                    Spacer(modifier = Modifier.width(5.dp))
                                    com.barryburgle.gameapp.ui.tool.utils.Switch(state.backupBeforeUpdate) {
                                        onEvent(ToolEvent.SwitchBackupBeforeUpdate)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}