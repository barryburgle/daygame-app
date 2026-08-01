package com.barryburgle.gameapp.ui.tool

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.recording.RecordingStateEnum
import com.barryburgle.gameapp.service.recording.RecordingService
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@ExperimentalMaterial3Api
@Composable
fun RecordingsCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    val localContext = LocalContext.current.applicationContext
    // check if permission was accepted
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onEvent(ToolEvent.SwitchRecordingsEnabled)
        } else {
            // Android auto-denies after two refusals, so the dialog stops appearing: without this
            // the switch would look like it does nothing at all
            Toast.makeText(
                localContext,
                "Microphone permission needed, enable it in app settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    val textFieldHeight = 55.dp

    // using remember: RecordingsCard re-runs from the top every time something on screen changes
    // If we just said var typedFolder = state.recordingsFolder, every re-run would create a
    // brand-new variable and your half-typed text would be wiped constantly. remember fixes that:
    // the first time, it runs the lambda and stores the result; on every re-run it skips the lambda
    // and hands back the stored value
    // mutableStateOf wraps a value in an observable box Compose watches reads and writes to it:
    // when the text field writes a new value into the box, Compose knows exactly which parts of the
    // UI read that box and redraws just those
    var typedFolder by remember(state.recordingsFolder) { mutableStateOf(state.recordingsFolder) }
    var showConfirmation by remember { mutableStateOf(false) }
    val hasChange = typedFolder != state.recordingsFolder

    // show path change confirmation, warn user about recording files about to be moved
    if (showConfirmation) {
        // number of existing recordings
        val movingCount = RecordingService.listRecordings(state.recordingsFolder)
            .count { RecordingService.isAppRecording(it) }
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.shadow(elevation = 10.dp),
            onDismissRequest = { showConfirmation = false },
            title = { LargeTitleText(text = "Move recordings") },
            text = {
                LittleBodyText(
                    "Move $movingCount recording${if (movingCount == 1) "" else "s"} to ${typedFolder.ifBlank { SettingDao.DEFAULT_RECORDINGS_FOLDER }}?"
                )
            },
            confirmButton = {
                ConfirmButton {
                    onEvent(ToolEvent.SetRecordingsFolder(typedFolder))
                    showConfirmation = false
                }
            },
            dismissButton = {
                DismissButton { showConfirmation = false }
            }
        )
    }

    GenericSettingsCard("Recordings", modifier) {
        SwitchSetting(
            "Enable Live session recordings", state.recordingsEnabled,
            description = "Record audio during a Live Session and play it back from session cards"
        ) {
            if (state.recordingsEnabled) {
                if (RecordingService.state.value.state != RecordingStateEnum.IDLE) {
                    Toast.makeText(localContext, "Stop the recording first", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onEvent(ToolEvent.SwitchRecordingsEnabled)
                }
            } else if (ContextCompat.checkSelfPermission(
                    localContext,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onEvent(ToolEvent.SwitchRecordingsEnabled)
            } else {
                // enabling the feature is what asks for the mic, so the request arrives with context
                micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
        // a folder for a feature that is off is noise
        if (state.recordingsEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            LittleBodyText("Folder where Live Session recordings are stored")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(textFieldHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(230.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = typedFolder,
                        onValueChange = { typedFolder = it },
                        placeholder = { LittleBodyText("Insert here the recordings folder") },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.height(textFieldHeight),
                        singleLine = true
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconShadowButton(
                        onClick = {
                            if (RecordingService.state.value.state != RecordingStateEnum.IDLE) {
                                Toast.makeText(
                                    localContext,
                                    "Stop the recording first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (hasChange) {
                                showConfirmation = true
                            }
                        },
                        imageVector = Icons.Default.Check,
                        contentDescription = if (hasChange) "Apply recordings folder" else "unavailable",
                        iconColor = if (hasChange) MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.38f)
                    )
                }
            }
        }
    }
}
