package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.model.enums.HeatmapEntityEnum
import com.barryburgle.gameapp.ui.utilities.selection.GenericSelectingButton
import com.barryburgle.gameapp.ui.utilities.selection.ScrollableSorter

@Composable
fun ScrollableSelector(
    spaceFromLeft: Dp,
    values: Array<HeatmapEntityEnum>,
    selected: FieldEnum,
    onClick: (FieldEnum) -> Unit
) {
    ScrollableSorter(
        spaceFromLeft
    ) {
        values.forEach { selectType ->
            selected.let {
                GenericSelectingButton(
                    it, selectType, onClick = { onClick(selectType) })
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}