package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun HourMaterialDialog(
    initialTime: LocalTime,
    description: String,
    hourDialogState: MaterialDialogState,
    onValueChange: (String) -> Unit
) {
    MaterialDialog(
        dialogState = hourDialogState, elevation = 10.dp, buttons = {
            positiveButton(
                "Ok", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            negativeButton(
                "Cancel", textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }, shape = MaterialTheme.shapes.extraLarge
    ) {
        this.timepicker(
            initialTime = initialTime,
            title = "Set $description's hour",
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.background,
                activeBackgroundColor = MaterialTheme.colorScheme.surface,
                activeTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                inactiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            onValueChange(it.toString())
        }
    }
}