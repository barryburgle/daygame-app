package com.barryburgle.gameapp.ui.tool

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.database.interoperability.CsvService
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.DataExchangeTypeEnum
import com.barryburgle.gameapp.ui.tool.state.ToolsState

@Composable
fun DataExchangeCard(
    cardTitle: String, state: ToolsState, modifier: Modifier = Modifier
        .height(280.dp)
        .shadow(
            elevation = 5.dp, shape = MaterialTheme.shapes.large
        ), onEvent: (ToolEvent) -> Unit
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        val textFieldHeight = 55.dp
        val textFieldWidth = 230.dp
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
                        Text(
                            text = cardTitle, style = MaterialTheme.typography.titleLarge
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
                        modifier = Modifier.fillMaxWidth(0.65f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${cardTitle} folder:",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        OutlinedTextField(
                            value = if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportFolder
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importFolder
                            } else "",
                            onValueChange = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetExportFolder(it))
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetImportFolder(it))
                                }
                            },
                            placeholder = { Text(text = "Insert here the ${cardTitle.lowercase()} folder") },
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .height(textFieldHeight)
                                .width(textFieldWidth)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${cardTitle} sessions file name:",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        OutlinedTextField(
                            value = if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportFileName
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importFileName
                            } else "",
                            onValueChange = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetExportFileName(it))
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetImportFileName(it))
                                }
                            },
                            placeholder = { Text(text = "Insert here the ${cardTitle.lowercase()} file name") },
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .height(textFieldHeight)
                                .width(textFieldWidth)
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Spacer(modifier = Modifier.height(0.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var checked = if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportHeader
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importHeader
                            } else false
                            val buttonText: String =
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    "${cardTitle} Header"
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    "Header in file"
                                } else ""
                            Text(
                                text = buttonText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .height(15.dp)
                                    .width(90.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                            val thumbColor by animateColorAsState(
                                targetValue = if (checked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                                animationSpec = tween(durationMillis = 500)
                            )
                            val trackColor by animateColorAsState(
                                targetValue = if (checked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                                animationSpec = tween(durationMillis = 500)
                            )
                            Switch(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                    if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                        onEvent(ToolEvent.SetExportHeader(it))
                                    } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                        onEvent(ToolEvent.SetImportHeader(it))
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = thumbColor,
                                    checkedTrackColor = trackColor,
                                    uncheckedThumbColor = thumbColor,
                                    uncheckedTrackColor = trackColor
                                ),
                            )
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    CsvService.exportRows(
                                        state.exportFolder,
                                        state.exportFileName,
                                        state.abstractSessions,
                                        state.exportHeader
                                    )
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(
                                        ToolEvent.SetAbstractSessions(
                                            CsvService.importRows(
                                                state.importFolder,
                                                state.importFileName,
                                                state.importHeader
                                            )
                                        )
                                    )
                                }
                                Toast.makeText(
                                    localContext,
                                    "Successfully ${cardTitle.lowercase()}ed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            Text(text = cardTitle + "\nSessions",
                                textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}