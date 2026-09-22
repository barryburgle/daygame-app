package com.barryburgle.gameapp.ui.tool.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.DataExchangeTypeEnum
import com.barryburgle.gameapp.ui.input.dialog.text.DialogTextComponent

@Composable
fun FilenameComposable(
    cardTitle: String,
    icon: ImageVector?,
    tableTitle: String,
    textFieldColumnWidth: Dp,
    localContext: Context,
    filenamePlaceholder: String,
    buttonFunction: () -> Boolean,
    reloadFunction: () -> Unit,
    filenameOnEvent: (String) -> Unit
) {
    RowTitle(
        "${cardTitle} ${tableTitle}s file name:",
        "",
        textFieldColumnWidth
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DialogTextComponent(
                value = filenamePlaceholder,
                placeholder = "${cardTitle.lowercase()} ${tableTitle} file name",
                singleLine = true,
                disableDelete = true,
                firstCustomActionIcon = icon,
                firstCustomAction = {
                    val isValid = buttonFunction()
                    if (isValid) {
                        Toast.makeText(
                            localContext,
                            "Successfully ${cardTitle.lowercase()}ed ${tableTitle}s",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            localContext,
                            "Failed to ${cardTitle.lowercase()} ${tableTitle}s",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                secondCustomActionIcon =
                    if (DataExchangeTypeEnum.IMPORT.type.equals(
                            cardTitle,
                            ignoreCase = true
                        )
                    ) Icons.Default.Replay else null,
                secondCustomAction = {
                    if (DataExchangeTypeEnum.IMPORT.type.equals(cardTitle, ignoreCase = true)) {
                        reloadFunction()
                        Toast.makeText(
                            localContext,
                            "Reloaded filename",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else null
                }
            ) {
                filenameOnEvent(it)
            }
        }
    }
}