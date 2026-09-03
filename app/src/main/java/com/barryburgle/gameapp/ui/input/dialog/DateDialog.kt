package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.enums.DateTypeEnum
import com.barryburgle.gameapp.service.EntityService
import com.barryburgle.gameapp.ui.input.CounterColumn
import com.barryburgle.gameapp.ui.input.dialog.component.DialogTextComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.DialogConstant
import com.barryburgle.gameapp.ui.utilities.ToggleIcon
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.DialogFormSectionDescription
import com.barryburgle.gameapp.ui.utilities.dialog.DialogTimeFormSection
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun DateDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current.applicationContext
    var latestDateValue = state.date
    var latestStartHour = state.startHour
    var latestEndHour = state.endHour
    var leadsExpanded by remember { mutableStateOf(false) }
    var dateTypesExpanded by remember { mutableStateOf(false) }
    var locationTextFieldExpanded by remember { mutableStateOf(false) }
    var dateNumberStart =
        if (state.isAddingDate) 0 else state.editDate?.dateNumber
    var dateCostStart = if (state.isAddingDate) 0 else state.editDate?.cost
    var dateNumber by remember {
        mutableStateOf(if (dateNumberStart == null) 0 else dateNumberStart)
    }
    var dateCost by remember {
        mutableStateOf(if (dateCostStart == null) 0 else dateCostStart)
    }
    val stickingPointsPagerState = rememberPagerState(pageCount = { 2 })
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .shadow(elevation = 10.dp)
            .fillMaxHeight(0.85f),
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
                                    "Set date's:",
                                    DialogConstant.DESCRIPTION_FONT_SIZE
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            var leadIcon = Icons.Default.Add
                            Column(
                                modifier = Modifier.width(DialogConstant.LEAD_COLUMN_WIDTH - DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                            ) {
                                if (state.leadId == 0L) {
                                    DialogFormSectionDescription(
                                        "Add lead:",
                                        DialogConstant.DESCRIPTION_FONT_SIZE
                                    )
                                } else {
                                    val foundLead =
                                        state.allLeads.filter { lead -> lead.id == state.leadId }
                                    if (foundLead.size != 0) {
                                        val lead = foundLead.get(0)
                                        DialogFormSectionDescription(
                                            CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age,
                                            DialogConstant.DESCRIPTION_FONT_SIZE
                                        )
                                        leadIcon = Icons.Default.SwapHoriz
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.width(DialogConstant.ADD_LEAD_COLUMN_WIDTH)
                            ) {
                                IconShadowButton(
                                    onClick = {
                                        leadsExpanded = true
                                    },
                                    imageVector = leadIcon,
                                    contentDescription = "Add lead"
                                )
                            }
                            DropdownMenu(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(450.dp),
                                expanded = leadsExpanded,
                                onDismissRequest = { leadsExpanded = false }
                            ) {
                                state.allLeads.forEach { lead ->
                                    DropdownMenuItem(
                                        text = { LittleBodyText(CountryEnum.getFlagByAlpha3(lead.nationality) + " " + lead.name + " " + lead.age) },
                                        onClick = {
                                            onEvent(GameEvent.SetLeadId(lead.id))
                                            leadsExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogTimeFormSection(
                        state,
                        onEvent,
                        latestDateValue,
                        latestStartHour,
                        latestEndHour
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        DropdownMenu(
                            modifier = Modifier
                                .width(175.dp)
                                .height(280.dp),
                            expanded = dateTypesExpanded,
                            onDismissRequest = { dateTypesExpanded = false }
                        ) {
                            DateTypeEnum.values().forEach { dateType ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = DateTypeEnum.getIcon(dateType.getType()),
                                                contentDescription = state.dateType,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier
                                                    .height(15.dp)
                                            )
                                            LittleBodyText(
                                                dateType.getType()
                                                    .replaceFirstChar { it.uppercase() })
                                        }
                                    },
                                    onClick = {
                                        onEvent(GameEvent.SetDateType(dateType.getType()))
                                        dateTypesExpanded = false
                                    }
                                )
                            }
                        }
                        IconShadowButton(
                            onClick = {
                                dateTypesExpanded = true
                            },
                            imageVector = DateTypeEnum.getIcon(state.dateType),
                            contentDescription = "Date type",
                            title = if (state.dateType.isBlank()) "Date type" else state.dateType.replaceFirstChar { it.uppercase() },
                            color = MaterialTheme.colorScheme.primaryContainer,
                            iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        IconShadowButton(
                            onClick = {
                                locationTextFieldExpanded = !locationTextFieldExpanded
                            },
                            imageVector = Icons.Default.PinDrop,
                            contentDescription = "Location",
                            title = "Location",
                            color = MaterialTheme.colorScheme.primaryContainer,
                            iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        val clipboardManager: ClipboardManager = LocalClipboardManager.current
                        val localContext = LocalContext.current.applicationContext
                        IconShadowButton(
                            onClick = {
                                var tweetUrl: String = clipboardManager.getText()!!.toString()
                                if (tweetUrl.startsWith("https://x.com/")) {
                                    onEvent(GameEvent.SetTweetUrl(tweetUrl))
                                    Toast.makeText(
                                        localContext,
                                        "Copied tweet url",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Tweet Url",
                            title = "Tweet Url",
                            color = MaterialTheme.colorScheme.primaryContainer,
                            iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                        )
                    }
                }
                BasicAnimatedVisibility(
                    visibilityFlag = locationTextFieldExpanded,
                ) {
                    Spacer(modifier = Modifier.height(7.dp))
                    OutlinedTextField(
                        value = state.location,
                        onValueChange = { onEvent(GameEvent.SetLocation(it)) },
                        placeholder = { LittleBodyText("Location") },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.height(80.dp)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                }
                BasicAnimatedVisibility(
                    visibilityFlag = !locationTextFieldExpanded,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(7.dp))
                        HorizontalPager(
                            state = stickingPointsPagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) { page ->
                            when (page) {
                                0 -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CounterColumn(
                                            count = dateNumber,
                                            label = "Date",
                                            onIncrement = {
                                                dateNumber += 1
                                                onEvent(GameEvent.SetDateNumber(dateNumber.toString()))
                                            },
                                            onDecrement = {
                                                dateNumber -= 1
                                                onEvent(GameEvent.SetDateNumber(dateNumber.toString()))
                                            }
                                        )
                                        CounterColumn(
                                            count = dateCost,
                                            label = "€",
                                            onIncrement = {
                                                dateCost += 1
                                                onEvent(GameEvent.SetCost(dateCost.toString()))
                                            },
                                            onDecrement = {
                                                dateCost -= 1
                                                onEvent(GameEvent.SetCost(dateCost.toString()))
                                            }
                                        )
                                    }
                                }

                                else -> {
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            ToggleIcon(
                                                "pull",
                                                state.pull,
                                                false,
                                                R.drawable.pull_b
                                            ) {
                                                onEvent(GameEvent.SwitchPull)
                                            }
                                            ToggleIcon(
                                                "bounce",
                                                state.bounce,
                                                false,
                                                R.drawable.bounce_b
                                            ) {
                                                onEvent(GameEvent.SwitchBounce)
                                            }
                                            ToggleIcon(
                                                "kiss",
                                                state.kiss,
                                                false,
                                                R.drawable.kiss_b
                                            ) {
                                                onEvent(GameEvent.SwitchKiss)
                                            }
                                            ToggleIcon(
                                                "lay",
                                                state.lay,
                                                false,
                                                R.drawable.bed_b
                                            ) {
                                                onEvent(GameEvent.SwitchLay)
                                            }
                                            ToggleIcon(
                                                "recorded",
                                                state.recorded,
                                                true,
                                                R.drawable.microphone_b
                                            ) {
                                                onEvent(GameEvent.SwitchRecorded)
                                            }
                                        }
                                        DialogTextComponent(
                                            value = state.stickingPoints,
                                            placeholder = "sticking points",
                                            singleLine = false
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(2) { dotIndex ->
                                val isSelected = stickingPointsPagerState.currentPage == dotIndex
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
                } else if (state.leadId == 0L) {
                    Toast.makeText(localContext, "Please choose a lead", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onEvent(GameEvent.SaveDate)
                    onEvent(GameEvent.SetIsInOverlayToFalse)
                    onEvent(GameEvent.HideDialog)
                    onEvent(GameEvent.SwitchJustSaved)
                    Toast.makeText(localContext, "Date saved", Toast.LENGTH_SHORT).show()
                }
            }
        },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideDialog)
            }
        }
    )
}