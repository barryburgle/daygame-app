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
import com.barryburgle.gameapp.model.date.DateModel
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.input.dialog.text.WavyTextComponent
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun DateModelEditDialog(
    onEvent: (GameEvent) -> Unit, dateModel: DateModel? = null
) {
    val context = LocalContext.current
    val localContext = context.applicationContext
    var showDeleteDateModelDialog by remember { mutableStateOf(false) }
    if (showDeleteDateModelDialog && dateModel != null) {
        DeleteConfirmationDialog(
            "date model",
            "Do you want to delete the ${dateModel.title} date model?",
            onConfirmRequest = {
                onEvent(GameEvent.DeleteDateModel(dateModel!!.id))
                onEvent(GameEvent.SwitchJustSaved)
                onEvent(GameEvent.HideDateModelDialog)
                showDeleteDateModelDialog = false
            },
            onDismissRequest = {
                showDeleteDateModelDialog = false
            },
        )
    }

    var title by remember(dateModel) { mutableStateOf(dateModel?.title ?: "") }
    var description by remember(dateModel) { mutableStateOf(dateModel?.description ?: "") }
    var phases by remember(dateModel) { mutableStateOf(dateModel?.phases ?: emptyList()) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(GameEvent.HideDateModelDialog)
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dialogTitle =
                    if (dateModel == null) "Add a new" else "Edit the \"${dateModel.title}\""
                LargeTitleText("${dialogTitle} date model")
                IconShadowButton(
                    onClick = {
                        showDeleteDateModelDialog = true
                    },
                    imageVector = Icons.Default.Delete,
                    iconColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = "Delete date model"
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WavyTextComponent(
                    value = title, placeholder = "Date model title"
                ) {
                    title = it
                }
                WavyTextComponent(
                    value = description, placeholder = "Date model description", singleLine = false
                ) {
                    description = it
                }
                // TODO: list of checkbox for selecting phases
            }
        },
        confirmButton = {
            ConfirmButton {
                if (title.isBlank()) {
                    Toast.makeText(localContext, "Please enter a title", Toast.LENGTH_SHORT).show()
                } else {
                    val dateModelToSave = DateModel(
                        title = title,
                        description = description,
                        // TODO: save new phases
                    )
                    if (dateModel != null) {
                        dateModelToSave.id = dateModel.id
                        dateModelToSave.phases = dateModel.phases
                    }
                    onEvent(
                        GameEvent.SaveDateModel(dateModelToSave)
                    )
                    onEvent(GameEvent.SwitchJustSaved)
                    onEvent(GameEvent.HideDateModelDialog)
                    Toast.makeText(localContext, "Date model saved", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.HideDateModelDialog)
            }
        })
}