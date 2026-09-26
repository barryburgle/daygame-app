package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.ui.utilities.selection.GenericSelectingButton

@Composable
fun <T : FieldEnum> ScrollableSelector(
    values: List<T>,
    selected: T?,
    onClick: (T) -> Unit
) {
    if (selected != null) {
        values.forEach { selectType ->
            GenericSelectingButton(
                currentSelect = selected,
                selectType = selectType,
                onClick = { onClick(selectType) }
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}