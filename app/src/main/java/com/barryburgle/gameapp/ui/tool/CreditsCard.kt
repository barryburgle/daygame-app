package com.barryburgle.gameapp.ui.tool

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.exchange.DataExchangeService
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.BasicAnimatedVisibility
import com.barryburgle.gameapp.ui.utilities.setting.ImageButtonSetting
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import dev.jeziellago.compose.markdowntext.MarkdownText

@ExperimentalMaterial3Api
@Composable
fun CreditsCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit,
    currentVersion: String,
    context: Context
) {
    val uriHandler = LocalUriHandler.current
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
                        LargeTitleText("Credits")
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
                            Spacer(modifier = Modifier.height(10.dp))
                            versionInfo(
                                state,
                                currentVersion,
                                onEvent,
                                context
                            )
                            ImageButtonSetting(
                                text = "Daygame App Github repository",
                                icon = R.drawable.ic_launcher_round,
                                contentDescription = "Project repository",
                                color = Color.Black,
                                onClick = {
                                    uriHandler.openUri("https://github.com/barryburgle/daygame-app")
                                })
                            Spacer(modifier = Modifier.height(5.dp))
                            ImageButtonSetting(
                                text = "Barry Burgle's blog",
                                icon = R.drawable.bb_v3b,
                                contentDescription = "Barry Blog",
                                color = Color.Black,
                                onClick = {
                                    uriHandler.openUri("https://barryburgle.wordpress.com/")
                                })
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
        var info = "Daygame App v$currentVersion"
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