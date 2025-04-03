package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.barryburgle.gameapp.model.enums.TimeInputFormEnum
import com.barryburgle.gameapp.service.FormatService
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun TimeInputFormButton(
    stateValue: String,
    timeInputFormEnum: TimeInputFormEnum,
    initialValue: String,
    description: String,
    buttonTextPrefix: String,
    onValueChange: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    when (timeInputFormEnum) {
        TimeInputFormEnum.DATE ->
            DateMaterialDialog(
                FormatService.parseDate(initialValue),
                description,
                dialogState
            ) {
                onValueChange(it)
            }

        TimeInputFormEnum.HOUR -> HourMaterialDialog(
            FormatService.parseTime(initialValue),
            description,
            dialogState
        ) {
            onValueChange(it)
        }
    }
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), onClick = { dialogState.show() }) {
        Text(
            text = getButtonTitle(
                timeInputFormEnum,
                buttonTextPrefix,
                stateValue
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun getEmptyButtonTitle(
    timeInputFormEnum: TimeInputFormEnum,
    buttonTextPrefix: String
): String {
    var timeTypePrefix = if (buttonTextPrefix.isEmpty()) "Set " else "$buttonTextPrefix "
    return timeTypePrefix + timeInputFormEnum.getField()
}

private fun getButtonTitle(
    timeInputFormEnum: TimeInputFormEnum,
    buttonTextPrefix: String,
    stateValue: String
): String {
    if (stateValue != null && stateValue.isNotBlank()) {
        return stateValue
    }
    return getEmptyButtonTitle(
        timeInputFormEnum,
        buttonTextPrefix
    )
}