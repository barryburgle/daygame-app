package com.barryburgle.gameapp.ui.tool

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.DataExchangeTypeEnum
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.tool.utils.FilenameComposable
import com.barryburgle.gameapp.ui.tool.utils.RowTitle
import java.io.FileNotFoundException

@Composable
fun DataExchangeCard(
    cardTitle: String,
    cardSubtitle: String,
    state: ToolsState,
    modifier: Modifier,
    sessionCsvService: SessionCsvService,
    leadCsvService: LeadCsvService,
    dateCsvService: DateCsvService,
    onEvent: (ToolEvent) -> Unit
) {
    // TODO: all icons in this caed should be round
    var icon: ImageVector? = null
    if (DataExchangeTypeEnum.EXPORT.type.equals(cardTitle)) {
        icon = Icons.Default.Upload
    } else if (DataExchangeTypeEnum.IMPORT.type.equals(cardTitle)) {
        icon = Icons.Default.Download
    }
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = MaterialTheme.shapes.large
    ) {
        val textFieldHeight = 55.dp
        val textFieldColumnWidth = 250.dp
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
                        Button(colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), onClick = {
                            if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                exportAll(
                                    cardTitle,
                                    sessionCsvService,
                                    state,
                                    leadCsvService,
                                    dateCsvService,
                                    localContext
                                )
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                importAll(
                                    cardTitle,
                                    sessionCsvService,
                                    state,
                                    leadCsvService,
                                    dateCsvService,
                                    localContext,
                                    onEvent
                                )
                            }
                        }) {
                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = cardTitle,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.height(25.dp)
                                )
                                Spacer(modifier = Modifier.width(7.dp))
                            }
                            Text(
                                text = cardTitle + " all", textAlign = TextAlign.Center
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
                        Text(
                            text = cardSubtitle, style = MaterialTheme.typography.bodySmall
                        )
                        RowTitle(
                            "${cardTitle} folder:", "Header:", textFieldColumnWidth
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(textFieldHeight),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.width(textFieldColumnWidth),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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
                                    modifier = Modifier.height(textFieldHeight)
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                var checked = if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    state.exportHeader
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    state.importHeader
                                } else false
                                val thumbColor by animateColorAsState(
                                    targetValue = if (checked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                                    animationSpec = tween(durationMillis = 500)
                                )
                                val trackColor by animateColorAsState(
                                    targetValue = if (checked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                                    animationSpec = tween(durationMillis = 500)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Spacer(modifier = Modifier.width(0.dp))
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
                            }
                        }
                        FilenameComposable(cardTitle = cardTitle,
                            icon,
                            "session",
                            textFieldColumnWidth,
                            textFieldHeight,
                            localContext,
                            if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportSessionsFileName
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importSessionsFileName
                            } else "",
                            buttonFunction = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    sessionCsvService.setExportObjects(state.abstractSessions)
                                    sessionCsvService.exportRows(
                                        state.exportFolder,
                                        state.exportSessionsFileName,
                                        state.exportHeader
                                    )
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    try {
                                        onEvent(
                                            ToolEvent.SetAbstractSessions(
                                                sessionCsvService.importRows(
                                                    state.importFolder,
                                                    state.importSessionsFileName,
                                                    state.importHeader
                                                )
                                            )
                                        )
                                    } catch (fileNotFoundException: FileNotFoundException) {
                                        Toast.makeText(
                                            localContext,
                                            fileNotFoundException.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            filenameOnEvent = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetExportSessionsFileName(it))
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetImportSessionsFileName(it))
                                }
                            })
                        FilenameComposable(cardTitle = cardTitle,
                            icon,
                            "lead",
                            textFieldColumnWidth,
                            textFieldHeight,
                            localContext,
                            if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportLeadsFileName
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importLeadsFileName
                            } else "",
                            buttonFunction = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    leadCsvService.setExportObjects(state.leads)
                                    leadCsvService.exportRows(
                                        state.exportFolder,
                                        state.exportLeadsFileName,
                                        state.exportHeader
                                    )
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    try {
                                        onEvent(
                                            ToolEvent.SetLeads(
                                                leadCsvService.importRows(
                                                    state.importFolder,
                                                    state.importLeadsFileName,
                                                    state.importHeader
                                                )
                                            )
                                        )
                                    } catch (fileNotFoundException: FileNotFoundException) {
                                        Toast.makeText(
                                            localContext,
                                            fileNotFoundException.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            filenameOnEvent = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetExportLeadsFileName(it))
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetImportLeadsFileName(it))
                                }
                            })
                        FilenameComposable(cardTitle = cardTitle,
                            icon,
                            "date",
                            textFieldColumnWidth,
                            textFieldHeight,
                            localContext,
                            if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                state.exportDatesFileName
                            } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                state.importDatesFileName
                            } else "",
                            buttonFunction = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    dateCsvService.setExportObjects(state.dates)
                                    dateCsvService.exportRows(
                                        state.exportFolder,
                                        state.exportDatesFileName,
                                        state.exportHeader
                                    )
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    try {
                                        onEvent(
                                            ToolEvent.SetDates(
                                                dateCsvService.importRows(
                                                    state.importFolder,
                                                    state.importDatesFileName,
                                                    state.importHeader
                                                )
                                            )
                                        )
                                    } catch (fileNotFoundException: FileNotFoundException) {
                                        Toast.makeText(
                                            localContext,
                                            fileNotFoundException.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            filenameOnEvent = {
                                if (DataExchangeTypeEnum.EXPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetExportDatesFileName(it))
                                } else if (DataExchangeTypeEnum.IMPORT.type == cardTitle) {
                                    onEvent(ToolEvent.SetImportDatesFileName(it))
                                }
                            })
                    }
                }
            }
        }
    }
}

private fun exportAll(
    cardTitle: String,
    sessionCsvService: SessionCsvService,
    state: ToolsState,
    leadCsvService: LeadCsvService,
    dateCsvService: DateCsvService,
    localContext: Context
) {
    sessionCsvService.setExportObjects(state.abstractSessions)
    sessionCsvService.exportRows(
        state.exportFolder,
        state.exportSessionsFileName,
        state.exportHeader
    )
    leadCsvService.setExportObjects(state.leads)
    leadCsvService.exportRows(
        state.exportFolder,
        state.exportLeadsFileName,
        state.exportHeader
    )
    dateCsvService.setExportObjects(state.dates)
    dateCsvService.exportRows(
        state.exportFolder,
        state.exportDatesFileName,
        state.exportHeader
    )
    Toast.makeText(
        localContext,
        "Successfully ${cardTitle.lowercase()}ed all tables",
        Toast.LENGTH_SHORT
    ).show()
}

private fun importAll(
    cardTitle: String,
    sessionCsvService: SessionCsvService,
    state: ToolsState,
    leadCsvService: LeadCsvService,
    dateCsvService: DateCsvService,
    localContext: Context,
    onEvent: (ToolEvent) -> Unit
) {
    try {
        onEvent(
            ToolEvent.SetAbstractSessions(
                sessionCsvService.importRows(
                    state.importFolder,
                    state.importSessionsFileName,
                    state.importHeader
                )
            )
        )
        onEvent(
            ToolEvent.SetLeads(
                leadCsvService.importRows(
                    state.importFolder,
                    state.importLeadsFileName,
                    state.importHeader
                )
            )
        )
        onEvent(
            ToolEvent.SetDates(
                dateCsvService.importRows(
                    state.importFolder,
                    state.importDatesFileName,
                    state.importHeader
                )
            )
        )
    } catch (fileNotFoundException: FileNotFoundException) {
        Toast.makeText(
            localContext,
            fileNotFoundException.message,
            Toast.LENGTH_SHORT
        ).show()
    }
    Toast.makeText(
        localContext,
        "Successfully ${cardTitle.lowercase()}ed all tables",
        Toast.LENGTH_SHORT
    ).show()
}