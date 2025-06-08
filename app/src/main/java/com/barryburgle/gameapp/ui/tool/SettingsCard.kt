package com.barryburgle.gameapp.ui.tool

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun SettingsCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit,
    currentVersion: String,
    context: Context
) {
    val textFieldColumnWidth = 230.dp
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
                    versionInfo(
                        state,
                        currentVersion,
                        onEvent,
                        context
                    )
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        onClick = { notificationHourDialogState.show() }) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = "Reminder",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .height(25.dp)
                                        )
                                        Spacer(modifier = Modifier.width(7.dp))
                                        LittleBodyText("Set sticking points reminder")
                                    }
                                }
                            }
                        }
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                LittleBodyText("Generate iDate on set creation")
                                Spacer(modifier = Modifier.width(5.dp))
                                com.barryburgle.gameapp.ui.tool.utils.Switch(state.generateiDate) {
                                    onEvent(ToolEvent.SwitchGenerateiDate)
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                LittleBodyText("Follow system theme")
                                Spacer(modifier = Modifier.width(5.dp))
                                com.barryburgle.gameapp.ui.tool.utils.Switch(state.themeSysFollow) {
                                    onEvent(ToolEvent.SwitchThemeSysFollow)
                                }
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
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth(0.6f)
                                        ) {
                                            Button(colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ), onClick = { themesExpanded = true }) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceAround,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Brush,
                                                        contentDescription = "Reminder",
                                                        tint = MaterialTheme.colorScheme.onPrimary,
                                                        modifier = Modifier
                                                            .height(25.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    LittleBodyText("Choose a theme")
                                                }
                                            }
                                            DropdownMenu(
                                                modifier = Modifier
                                                    .width(175.dp)
                                                    .height(280.dp),
                                                expanded = themesExpanded,
                                                onDismissRequest = { themesExpanded = false }
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
                                                                                    TextStyle(
                                                                                        textDecoration = TextDecoration.Underline
                                                                                    )
                                                                                )
                                                                        }
                                                                        // TODO: create LittleBodyText that manages underlying
                                                                        Text(
                                                                            text = themeName,
                                                                            style = currentStyle,
                                                                            color = MaterialTheme.colorScheme.onPrimary
                                                                        )
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
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun updateRedDot() {
    Row(
        modifier = Modifier
            .width(8.dp)
            .height(8.dp)
            .background(
                MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(5.dp)
            )
    ) {}
}

@Composable
fun versionInfo(
    state: ToolsState,
    currentVersion: String,
    onEvent: (ToolEvent) -> Unit,
    context: Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val latestVersion = state.latestAvailable
        var info = "Daygame App $currentVersion"
        val uriHandler = LocalUriHandler.current
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.latestAvailable != null && !state.latestAvailable.isEmpty()) {
                changelog(latestVersion, state, onEvent)
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(50.dp)
                        )
                ) {
                    IconButton(
                        onClick = {
                            if (state.backupBeforeUpdate) {
                                DataExchangeService.backup(state)
                                Toast.makeText(
                                    context,
                                    "Successfully backed up all tables",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            uriHandler.openUri(state.latestDownloadUrl)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Update",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            } else {
                LittleBodyText(info)
            }
        }
    }
    if (state.showChangelog) {
        Spacer(modifier = Modifier.height(12.dp))
    }
    BasicAnimatedVisibility(
        visibilityFlag = state.showChangelog,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MarkdownText(
                    markdown = state.latestChangelog
                        .split("\n")
                        .drop(2)
                        .joinToString("\n"),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun changelog(
    newVersion: String,
    state: ToolsState,
    onEvent: (ToolEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        updateRedDot()
        Spacer(modifier = Modifier.width(5.dp))
        LittleBodyText("Update to $newVersion. Changelog:")
        IconButton(onClick = {
            onEvent(ToolEvent.SwitchShowChangelog)
        }) {
            Icon(
                imageVector = if (state.showChangelog) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Changelog",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(50.dp)
            )
        }
    }
}