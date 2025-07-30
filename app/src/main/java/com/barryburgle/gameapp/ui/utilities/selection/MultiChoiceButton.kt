package com.barryburgle.gameapp.ui.utilities.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiChoiceButton(
    options: List<String>,
    sizes: List<Int>,
    modifier: Modifier,
    selectedOptions: SnapshotStateList<Boolean>,
    onCheckedChange: (Int) -> Unit
) {
    val selectedOptionsToDisplay = selectedOptions
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center
    ) {
        MultiChoiceSegmentedButtonRow(
            modifier = modifier
                .height(35.dp)
                .shadow(
                    elevation = 15.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
                )
        ) {
            options.fastForEachIndexed { index, label ->
                val count: String = if (sizes[index] < 100) sizes[index].toString() else "99+"
                SegmentedButton(
                    border = BorderStroke(0.dp, Color.Transparent),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    checked = selectedOptionsToDisplay[index],
                    onCheckedChange = {
                        selectedOptionsToDisplay[index] = !selectedOptionsToDisplay[index]
                        onCheckedChange(index)
                    },
                    label = {
                        Text(label)
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                )
            }
        }
    }
}