package com.barryburgle.gameapp.ui.tool

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.enums.ThemeEnum
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.dropdown.Dropdown
import com.barryburgle.gameapp.ui.utilities.dropdown.SelectableOption
import com.barryburgle.gameapp.ui.utilities.setting.IconButtonSetting
import com.barryburgle.gameapp.ui.utilities.setting.SwitchSetting

@ExperimentalMaterial3Api
@Composable
fun ThemeCard(
    state: ToolsState, modifier: Modifier, onEvent: (ToolEvent) -> Unit
) {
    var themesExpanded by remember { mutableStateOf(false) }
    val localContext = LocalContext.current.applicationContext
    GenericSettingsCard("Theme", modifier) {
        SwitchSetting(
            "Follow system theme",
            state.themeSysFollow,
            if (state.themeSysFollow) "The app theme is set following system theme" else "To choose a different theme from ${state.theme.replaceFirstChar { it.uppercase() }} use the option below"
        ) {
            onEvent(ToolEvent.SwitchThemeSysFollow)
        }
        IconButtonSetting(
            text = "Choose a theme",
            imageVector = Icons.Default.Brush,
            contentDescription = "Choose theme",
            onClick = { themesExpanded = true })

        Dropdown(
            modifier = Modifier
                .width(200.dp)
                .height(280.dp),
            expanded = themesExpanded,
            onDismissRequest = { themesExpanded = false },
            items = ThemeEnum.sortedValues(),
            onItemClick = { theme ->
                onEvent(ToolEvent.SetTheme(theme.type))
                Toast.makeText(
                    localContext,
                    "${theme.type.replaceFirstChar { it.uppercase() }} theme set",
                    Toast.LENGTH_SHORT
                ).show()
            }) { theme ->
            SelectableOption(customContent = {
                drawThemeColors(
                    theme = theme,
                    isSelected = state.theme == theme.type
                )
            }, optionName = theme.type.replaceFirstChar { it.uppercase() })
        }
        Spacer(modifier = Modifier.height(5.dp))
        IconButtonSetting(
            text = "Pick random theme",
            imageVector = Icons.Default.Shuffle,
            contentDescription = "Random theme",
            onClick = {
                val randomTheme = ThemeEnum.randomValue(state.theme).type
                onEvent(ToolEvent.SetTheme(randomTheme))
                Toast.makeText(
                    localContext,
                    "${randomTheme.replaceFirstChar { it.uppercase() }} theme set",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }
}

@Composable
private fun drawThemeColors(theme: ThemeEnum, isSelected: Boolean) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .background(
                    theme.getThirdHint(), shape = RoundedCornerShape(20.dp)
                )
                .height(30.dp)
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Canvas(
                modifier = Modifier.size(25.dp)
            ) {
                drawCircle(
                    color = theme.getFirstHint(), radius = size.minDimension / 2f
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Canvas(
                modifier = Modifier.size(25.dp)
            ) {
                drawCircle(
                    color = theme.getSecondHint(), radius = size.minDimension / 2f
                )
            }
        }
        if (isSelected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6.dp))
            ) {
                val color = MaterialTheme.colorScheme.onPrimary
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = color, radius = size.minDimension / 2f
                    )
                }
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides theme.getSecondHint()
                ) {
                    SegmentedButtonDefaults.Icon(true)
                }
            }
        }
    }
}