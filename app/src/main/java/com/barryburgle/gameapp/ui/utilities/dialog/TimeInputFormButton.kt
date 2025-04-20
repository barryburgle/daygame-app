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
import java.time.LocalDateTime

@Composable
fun TimeInputFormButton(
    stateValue: String,
    latestDateValue: String,
    timeInputFormEnum: TimeInputFormEnum,
    isAddingEvent: Boolean,
    editEvent: Any?,
    possibleReturn: String,
    description: String,
    buttonTextPrefix: String,
    onValueChange: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    val initialValue = passInitialValue(isAddingEvent, editEvent, possibleReturn)
    when (timeInputFormEnum) {
        TimeInputFormEnum.DATE -> DateMaterialDialog(
            FormatService.parseDate(initialValue), description, dialogState
        ) {
            onValueChange(it)
        }

        TimeInputFormEnum.HOUR -> HourMaterialDialog(
            FormatService.parseTime(initialValue), description, dialogState
        ) {
            onValueChange(it)
        }
    }
    val displayValue = passStateValue(stateValue, latestDateValue)
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), onClick = { dialogState.show() }) {
        Text(
            text = getButtonTitle(
                timeInputFormEnum, buttonTextPrefix, displayValue
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun getEmptyButtonTitle(
    timeInputFormEnum: TimeInputFormEnum, buttonTextPrefix: String
): String {
    var timeTypePrefix = if (buttonTextPrefix.isEmpty()) "Set " else "$buttonTextPrefix "
    return timeTypePrefix + timeInputFormEnum.getField()
}

private fun getButtonTitle(
    timeInputFormEnum: TimeInputFormEnum, buttonTextPrefix: String, stateValue: String
): String {
    if (stateValue != null && stateValue.isNotBlank()) {
        return stateValue
    }
    return getEmptyButtonTitle(
        timeInputFormEnum, buttonTextPrefix
    )
}

private fun passStateValue(stateValue: String, latestValue: String): String {
    if (stateValue != latestValue && latestValue.isNotEmpty()) return latestValue
    return stateValue
}

private fun passInitialValue(
    isAddingEvent: Boolean,
    editEvent: Any?,
    possibleReturn: String
): String {
    if (isAddingEvent || editEvent == null)
        return LocalDateTime.now().toString().substring(0, 16) + "Z"
    return possibleReturn
}