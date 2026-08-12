package com.barryburgle.gameapp.ui.stats

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.ui.stats.chart.PinPointScatterChart
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.body.MediumBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@ExperimentalMaterial3Api
@Composable
fun PinPointScatterCard(
    modifier: Modifier,
    title: String,
    statCardIcon: ImageVector,
    description: String,
    state: StatsState,
    onEvent: (StatsEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedTypes = state.pinPointsTypeSelectionList.filterIsInstance<PinPointTypeEnum>()
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = statCardIcon,
                                contentDescription = title,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            LargeTitleText(title)
                        }
                        Box {
                            IconShadowButton(
                                onClick = { expanded = true },
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter Types"
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                PinPointTypeEnum.entries.forEach { type ->
                                    val isChecked = selectedTypes.contains(type)
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Checkbox(
                                                    checked = isChecked,
                                                    onCheckedChange = null
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                MediumBodyText(type.getField()
                                                    .replaceFirstChar { it.uppercase() })
                                            }
                                        },
                                        onClick = {
                                            val newList = if (isChecked) {
                                                if (selectedTypes.size > 1) selectedTypes - type else selectedTypes
                                            } else {
                                                selectedTypes + type
                                            }
                                            onEvent(StatsEvent.SelectPinPointType(newList))
                                        }
                                    )
                                }
                            }
                        }
                    }
                    LittleBodyText(description)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        PinPointScatterChart(
                            pinPoints = state.typeFilteredPinPoints
                        )
                    }
                }
            }
        }
    }
}