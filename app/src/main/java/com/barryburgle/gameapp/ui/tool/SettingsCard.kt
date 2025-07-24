package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting
import com.barryburgle.gameapp.ui.utilities.setting.IconButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun SettingsCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    var themesExpanded by remember { mutableStateOf(false) }
    val notificationHourDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = notificationHourDialogState,
        elevation = 10.dp,
        buttons = {
            positiveButton(
                "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Set notification hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.onPrimary,
                activeBackgroundColor = MaterialTheme.colorScheme.tertiary,
                activeTextColor = MaterialTheme.colorScheme.background,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onEvent(ToolEvent.SetNotificationTime(it.toString().substring(0, 5)))
        }
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LargeTitleText("Settings")
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InputCountComponent(
                            inputTitle = "Average",
                            modifier = Modifier,
                            style = MaterialTheme.typography.displaySmall,
                            onEvent = onEvent as (GenericEvent) -> Unit,
                            countStart = state.lastSessionAverageQuantity,
                            saveEvent = ToolEvent::SetLastSessionAverageQuantity
                        )
                        InputCountComponent(
                            inputTitle = "Sessions",
                            modifier = Modifier,
                            style = MaterialTheme.typography.displaySmall,
                            onEvent = onEvent as (GenericEvent) -> Unit,
                            countStart = state.lastSessionsShown,
                            saveEvent = ToolEvent::SetLastSessionsShown
                        )
                        InputCountComponent(
                            inputTitle = "Weeks",
                            modifier = Modifier,
                            style = MaterialTheme.typography.displaySmall,
                            onEvent = onEvent as (GenericEvent) -> Unit,
                            countStart = state.lastWeeksShown,
                            saveEvent = ToolEvent::SetLastWeeksShown
                        )
                        InputCountComponent(
                            inputTitle = "Months",
                            modifier = Modifier,
                            style = MaterialTheme.typography.displaySmall,
                            onEvent = onEvent as (GenericEvent) -> Unit,
                            countStart = state.lastMonthsShown,
                            saveEvent = ToolEvent::SetLastMonthsShown
                        )
                    }
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
                            IconButtonSetting(text = "Set sticking points reminder",
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Reminder",
                                onClick = { notificationHourDialogState.show() })
                            SwitchSetting(
                                "Generate iDate on set creation", state.generateiDate,
                                description = "A new iDate will be generated when a set is inserted with the iDate flag set to true"
                            ) {
                                onEvent(ToolEvent.SwitchGenerateiDate)
                            }
                            SwitchSetting(
                                "Follow count",
                                state.followCount,
                                description = "When inserting a session increase sets if conversations is increased, increase sets and conversations if contacts is increased, increase sets conversations and contacts if a new lead is inserted"
                            ) {
                                onEvent(ToolEvent.SwitchFollowCount)
                            }
                            SwitchSetting(
                                "Suggest leads nationality",
                                state.suggestLeadsNationality,
                            ) {
                                onEvent(ToolEvent.SwitchSuggestLeadsNationality)
                            }
                            CountSetting(
                                text = "Show ${state.shownNationalities} nationalities",
                                count = state.shownNationalities,
                                description = "Show only ${state.shownNationalities} countries in nationalities bar charts and country selector on lead insertion (min 1 to max 8)",
                                onEvent = onEvent as (GenericEvent) -> Unit,
                                saveEvent = ToolEvent::SetShownNationalities
                            )
                            SwitchSetting(
                                "Follow system theme", state.themeSysFollow
                            ) {
                                onEvent(ToolEvent.SwitchThemeSysFollow)
                            }
                            IconButtonSetting(text = "Choose a theme",
                                imageVector = Icons.Default.Brush,
                                contentDescription = "Choose theme",
                                onClick = { themesExpanded = true })
                            DropdownMenu(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(280.dp),
                                expanded = themesExpanded,
                                onDismissRequest = { themesExpanded = false },
                                offset = DpOffset(100.dp, 0.dp)
                            ) {
                                ThemeEnum.sortedValues().forEach { theme ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth(
                                                        0.7f
                                                    )
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .background(
                                                                theme.getThirdHint(),
                                                                shape = RoundedCornerShape(
                                                                    20.dp
                                                                )
                                                            )
                                                            .padding(3.dp)
                                                    ) {
                                                        Canvas(
                                                            modifier = Modifier.size(
                                                                12.dp
                                                            )
                                                        ) {
                                                            drawCircle(
                                                                color = theme.getFirstHint(),
                                                                radius = size.minDimension / 2f
                                                            )
                                                        }
                                                        Spacer(
                                                            modifier = Modifier.width(
                                                                2.dp
                                                            )
                                                        )
                                                        Canvas(
                                                            modifier = Modifier.size(
                                                                12.dp
                                                            )
                                                        ) {
                                                            drawCircle(
                                                                color = theme.getSecondHint(),
                                                                radius = size.minDimension / 2f
                                                            )
                                                        }
                                                    }
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        var themeName =
                                                            theme.type.replaceFirstChar { it.uppercase() }
                                                        var currentStyle =
                                                            MaterialTheme.typography.bodySmall
                                                        if (state.theme == theme.type) {
                                                            currentStyle =
                                                                currentStyle.merge(
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                        }
                                                        Text(
                                                            text = themeName,
                                                            style = currentStyle,
                                                            color = MaterialTheme.colorScheme.onPrimary
                                                        )
                                                        if (state.theme == theme.type) {
                                                            Spacer(
                                                                modifier = Modifier.width(
                                                                    10.dp
                                                                )
                                                            )
                                                            Box(
                                                                contentAlignment = Alignment.Center,
                                                                modifier = Modifier.size(
                                                                    20.dp
                                                                )
                                                            ) {
                                                                val color =
                                                                    MaterialTheme.colorScheme.primaryContainer
                                                                Canvas(modifier = Modifier.matchParentSize()) {
                                                                    drawCircle(
                                                                        color = color,
                                                                        radius = size.minDimension / 2f
                                                                    )
                                                                }
                                                                SegmentedButtonDefaults.Icon(
                                                                    true
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onClick = {
                                            onEvent(ToolEvent.SetTheme(theme.type))
                                            themesExpanded = false
                                        }
                                    )
                                }
                            }
                            // TODO: create a "Share" setting card in future, and divide the settings in the current "Setting" card across different cards
                            SwitchSetting(
                                "Share simpler report for +1s", state.simplePlusOneReport,
                                description = "Report without sticking points and date flags is generated for dates that ended up in a lay"
                            ) {
                                onEvent(ToolEvent.SwitchSimplePlusOneReport)
                            }
                            SwitchSetting(
                                "Never share lead info", state.neverShareLeadInfo
                            ) {
                                onEvent(ToolEvent.SwitchNeverShareLeadInfo)
                            }
                        }
                    }
                }
            }
        }
    }
}