package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DateMaterialDialog(
    initialDate: LocalDate,
    description: String,
    dateDialogState: MaterialDialogState,
    onValueChange: (String) -> Unit
) {
    MaterialDialog(
        dialogState = dateDialogState, elevation = 10.dp, buttons = {
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
        this.datepicker(
            initialDate = initialDate,
            title = "Set $description date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.onTertiary,
                dateInactiveTextColor = MaterialTheme.colorScheme.background

            )
        ) {
            onValueChange(it.toString())
        }
    }
}