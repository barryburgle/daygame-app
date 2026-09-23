package com.barryburgle.gameapp.ui.output.dialog

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.OutputEvent
import com.barryburgle.gameapp.model.ping.Ping
import com.barryburgle.gameapp.ui.input.dialog.text.DialogTextComponent
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun PingInsertDialog(
    onEvent: (OutputEvent) -> Unit,
) {
    val context = LocalContext.current
    val localContext = context.applicationContext
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var picUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                // Take persistent read permission so the URI remains accessible across app launches
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(it, takeFlags)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        picUri = uri
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(OutputEvent.HideAddPingDialog)
        },
        title = {
            LargeTitleText("Add a new ping")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogTextComponent(
                    value = title, placeholder = "ping title"
                ) {
                    title = it
                }
                DialogTextComponent(
                    value = body, placeholder = "ping text", onCopyClick = {
                        clipboardManager.setText(AnnotatedString(body))
                        Toast.makeText(localContext, "Ping text copied", Toast.LENGTH_SHORT).show()
                    }) {
                    body = it
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ToggleIcon(
                        flag = link.isNotBlank(),
                        icon = R.drawable.link,
                        dotCondition = link.startsWith("http")
                    ) {
                        val textFromClipboard = clipboardManager.getText()
                        if (textFromClipboard != null) {
                            val acquiredLink: String = textFromClipboard.toString()
                            if (acquiredLink.startsWith("http")) {
                                link = acquiredLink
                                Toast.makeText(
                                    localContext, "Ping URL copied", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    LittleBodyText("Tap on the link icon to copy the ping link from your clipboard")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ToggleIcon(
                        flag = picUri != null, icon = R.drawable.pic, dotCondition = picUri != null
                    ) {
                        imagePickerLauncher.launch("image/*")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    LittleBodyText("Tap on the pic icon to open and select the pic for the ping. No pic is stored in this app")
                }
            }
        },
        confirmButton = {
            ConfirmButton {
                if (title.isBlank()) {
                    Toast.makeText(localContext, "Please enter a title", Toast.LENGTH_SHORT).show()
                } else {
                    val ping = Ping(
                        title = title,
                        body = body.ifBlank { null },
                        link = link.ifBlank { null },
                        pic = if (picUri == null) null else picUri?.toString()
                    )
                    onEvent(
                        OutputEvent.SavePing(ping)
                    )
                    onEvent(OutputEvent.HideAddPingDialog)
                    Toast.makeText(localContext, "Ping saved", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(OutputEvent.HideAddPingDialog)
            }
        })
}