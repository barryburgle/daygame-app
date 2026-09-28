package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.date.DateModel
import com.barryburgle.gameapp.model.date.DatePhase
import com.barryburgle.gameapp.ui.input.card.DeleteConfirmationDialog
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.input.dialog.text.WavyTextComponent
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText

@Composable
fun DateModelEditDialog(
    onEvent: (GameEvent) -> Unit, dateModel: DateModel? = null, datePhases: List<DatePhase>
) {
    val context = LocalContext.current
    val localContext = context.applicationContext
    var showDeleteDateModelDialog by remember { mutableStateOf(false) }
    if (showDeleteDateModelDialog && dateModel != null) {
        DeleteConfirmationDialog(
            "date model",
            "Do you want to delete the ${dateModel.title} date model?",
            onConfirmRequest = {
                onEvent(GameEvent.DeleteDateModel(dateModel.id))
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

    FlowDialog(
        modifier = Modifier
            .shadow(elevation = 10.dp),
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
                    if (dateModel == null) "Add a new date" else "Edit the \"${dateModel.title}\""
                Column(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    LargeTitleText("${dialogTitle} model")
                }
                if (dateModel != null) {
                    IconShadowButton(
                        onClick = {
                            showDeleteDateModelDialog = true
                        },
                        imageVector = Icons.Default.Delete,
                        iconColor = MaterialTheme.colorScheme.onErrorContainer,
                        contentDescription = "Delete date model"
                    )
                }
            }
        }, onConfirm = {
            if (title.isBlank()) {
                Toast.makeText(localContext, "Please enter a title", Toast.LENGTH_SHORT).show()
            } else {
                val dateModelToSave = DateModel(
                    title = title,
                    description = description,
                    phases = phases
                )
                if (dateModel != null) {
                    dateModelToSave.id = dateModel.id
                    dateModelToSave.phases = phases
                }
                onEvent(
                    GameEvent.SaveDateModel(dateModelToSave)
                )
                onEvent(GameEvent.SwitchJustSaved)
                onEvent(GameEvent.HideDateModelDialog)
                Toast.makeText(localContext, "Date model saved", Toast.LENGTH_SHORT).show()
            }
        }
    ) { contentPadding ->
        val layoutDirection = LocalLayoutDirection.current
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    top = contentPadding.calculateTopPadding() + 8.dp,
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                ),
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
            if (datePhases.isNotEmpty()) {
                LittleBodyText(text = "Select the date phases:")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                    ) {
                        items(items = datePhases, key = { it.id }) { phase ->
                            val isChecked = phases.contains(phase.id)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        phases = if (isChecked) {
                                            phases - phase.id
                                        } else {
                                            phases + phase.id
                                        }
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.75f)
                                        .padding(
                                            start = 12.dp,
                                            top = 6.dp,
                                            bottom = 6.dp,
                                            end = 4.dp
                                        ),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    WavyPlaceholder(text = "Lasts ${phase.duration} minutes")
                                    MediumTitleText(phase.title)
                                    LittleBodyText(phase.description)
                                }
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        phases = if (checked) {
                                            phases + phase.id
                                        } else {
                                            phases - phase.id
                                        }
                                    },
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
