package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@Composable
fun DeleteCard(
    cardTitle: String,
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    var icon: ImageVector? = Icons.Default.DeleteForever
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
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
                        Text(
                            text = cardTitle, style = MaterialTheme.typography.titleLarge
                        )
                        Button(colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), onClick = {
                            onEvent(ToolEvent.SwitchIsCleaning)
                        }) {
                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = cardTitle,
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.height(25.dp)
                                )
                                Spacer(modifier = Modifier.width(7.dp))
                            }
                            Text(
                                text = "Clean", textAlign = TextAlign.Center
                            )
                        }
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Archive backup folder:",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    com.barryburgle.gameapp.ui.tool.utils.Switch(state.archiveBackupFolder) {
                                        onEvent(ToolEvent.SwitchArchiveBackupFolder)
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                var description =
                                    if (state.archiveBackupFolder) "The backup folder content will be to moved to /emulated/0/storage/${state.exportFolder}/${state.backupFolder}/${CSVFindService.ARCHIVE_FOLDER} before cleaning" else
                                        "The backup folder content will be progressively overwritten after ${state.lastBackup} data insertions. If you want to preserve backups from overwriting please enable the previous option"
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}