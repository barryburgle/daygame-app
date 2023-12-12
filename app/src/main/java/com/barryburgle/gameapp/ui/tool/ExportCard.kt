package com.barryburgle.gameapp.ui.tool

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.database.interoperability.CsvService
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.tool.state.ToolsState


@Composable
fun ExportCard(
    state: ToolsState,
    modifier: Modifier = Modifier
        .height(280.dp)
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
        val textFieldHeight = 55.dp
        val textFieldWidth = 230.dp
        val localContext = LocalContext.current.applicationContext
        Text(
            text = "Import/Export",
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
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.exportFolder,
                    onValueChange = { onEvent(ToolEvent.SetExportFolder(it)) },
                    placeholder = { Text(text = "Import/Export Folder") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .height(textFieldHeight)
                        .width(textFieldWidth)
                )
                OutlinedTextField(
                    value = state.exportFileName,
                    onValueChange = { onEvent(ToolEvent.SetExportFileName(it)) },
                    placeholder = { Text(text = "Export file name") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .height(textFieldHeight)
                        .width(textFieldWidth)
                )
                OutlinedTextField(
                    value = state.importFileName,
                    onValueChange = { onEvent(ToolEvent.SetImportFileName(it)) },
                    placeholder = { Text(text = "Import file name") },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .height(textFieldHeight)
                        .width(textFieldWidth)
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    CsvService.exportRows(
                        state.exportFolder,
                        state.exportFileName,
                        state.abstractSessions,
                        state.abstractSessionHeader
                    )
                    Toast.makeText(localContext, "Successfully exported", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Export")
                }
                Button(onClick = {
                    val importFileName = state.importFileName
                    onEvent(
                        ToolEvent.SetAbstractSessions(
                            CsvService.importRows(
                                state.exportFolder,
                                importFileName,
                                true, // TODO: settable by user interface
                                AbstractSession.separator
                            )
                        )
                    )
                    Toast.makeText(localContext, "Successfully imported", Toast.LENGTH_SHORT).show()
                    // TODO: insert filename import proposal according to correspondent user name given for export
                }) {
                    Text(text = "Import")
                }
            }
        }
    }
}