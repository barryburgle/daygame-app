package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.IconButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun ThemeCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    var themesExpanded by remember { mutableStateOf(false) }
    GenericSettingsCard("Theme", modifier) {
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
    }
}