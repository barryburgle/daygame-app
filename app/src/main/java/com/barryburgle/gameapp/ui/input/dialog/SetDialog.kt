package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.SetSortType
import com.barryburgle.gameapp.service.EntityService
import com.barryburgle.gameapp.ui.input.dialog.text.WavyTextComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.DialogTimeFormSection
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun SetDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    val pagerState = rememberPagerState(pageCount = { 2 })
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.shadow(elevation = 10.dp),
        onDismissRequest = {
            onEvent(GameEvent.SetIsInOverlayToFalse)
            onEvent(GameEvent.HideDialog)
        },
        title = {
            LargeTitleText(text = description)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogConstant.ADD_LEAD_COLUMN_WIDTH),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.width(DialogConstant.TIME_COLUMN_WIDTH)
                            ) {
                                DialogFormSectionDescription(
                                    "Set set's:",
                                    DialogConstant.DESCRIPTION_FONT_SIZE
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            var leadIcon = Icons.Default.Add
                            var editLead: Boolean = false
                            if (!state.isUpdatingSet) {
                                Column(
                                    modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                                ) {
                                    if (state.leads.isEmpty()) {
                                        DialogFormSectionDescription(
                                            "Add lead:",
                                            DialogConstant.DESCRIPTION_FONT_SIZE
                                        )
                                    } else {
                                        val lead = state.leads.get(0)
                                        val leadAgeDesc = if (lead.age != 0L) " ${lead.age}" else ""
                                        DialogFormSectionDescription(
                                            CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + leadAgeDesc,
                                            DialogConstant.DESCRIPTION_FONT_SIZE
                                        )
                                        leadIcon = Icons.Default.SwapHoriz
                                        editLead = true
                                    }
                                }
                                Column(
                                    modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                                ) {
                                    IconShadowButton(
                                        onClick = {
                                            if (editLead) {
                                                onEvent(GameEvent.EmptyLeads)
                                            }
                                            onEvent(GameEvent.ShowLeadDialog(true, false))
                                        },
                                        imageVector = leadIcon,
                                        contentDescription = "Add a lead"
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DialogTimeFormSection(
                        state,
                        onEvent,
                        latestDateValue,
                        latestStartHour,
                        latestEndHour
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        when (page) {
                            0 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(7.dp)
                                ) {
                                    val location = state.location
                                    WavyTextComponent(
                                        value = location,
                                        placeholder = "location",
                                        singleLine = true,
                                        onCopyClick = {
                                            clipboardManager.setText(AnnotatedString(location))
                                            Toast.makeText(
                                                localContext,
                                                "Location copied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    ) {
                                        onEvent(GameEvent.SetLocation(it))
                                    }
                                }
                            }

                            1 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(7.dp)
                                ) {
                                    val stickingPoints = state.stickingPoints
                                    WavyTextComponent(
                                        value = stickingPoints,
                                        placeholder = "sticking points",
                                        singleLine = false,
                                        onCopyClick = {
                                            clipboardManager.setText(AnnotatedString(stickingPoints))
                                            Toast.makeText(
                                                localContext,
                                                "Sticking points copied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    ) {
                                        onEvent(GameEvent.SetStickingPoints(it))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        repeat(2) { dotIndex ->
                            val isSelected = pagerState.currentPage == dotIndex
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 7.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.35f
                                        )
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ToggleIcon(
                        SetSortType.CONVERSATION.getField(),
                        state.conversation,
                        R.drawable.chat_b
                    ) {
                        onEvent(GameEvent.SwitchConversation)
                    }
                    ToggleIcon(
                        "contact",
                        state.contact,
                        R.drawable.contact_b
                    ) {
                        onEvent(GameEvent.SwitchContact)
                    }
                    ToggleIcon(
                        "instant\ndate",
                        state.instantDate,
                        R.drawable.idate_b
                    ) {
                        onEvent(GameEvent.SwitchInstantDate)
                        if (!state.instantDate && state.generateiDate) {
                            Toast.makeText(
                                localContext,
                                "Generating related iDate",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    ToggleIcon(
                        "recorded",
                        state.recorded,
                        R.drawable.microphone_b
                    ) {
                        onEvent(GameEvent.SwitchRecorded)
                    }
                }
            }
        },
        confirmButton = {
            ConfirmButton {
                if (EntityService.getParsedHour(
                        state.date,
                        state.startHour
                    ) > EntityService.getParsedHour(state.date, state.endHour)
                ) {
                    Toast.makeText(
                        localContext,
                        "Please choose valid hours",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onEvent(GameEvent.SaveSet)
                    onEvent(GameEvent.SetIsInOverlayToFalse)
                    onEvent(GameEvent.HideDialog)
                    onEvent(GameEvent.SwitchJustSaved)
                    Toast.makeText(localContext, "Set saved", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideDialog)
            }
        })
}
