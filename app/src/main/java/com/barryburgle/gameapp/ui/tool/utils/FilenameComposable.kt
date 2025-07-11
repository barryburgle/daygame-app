package com.barryburgle.gameapp.ui.tool.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.DataExchangeTypeEnum
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun FilenameComposable(
    cardTitle: String,
    icon: ImageVector?,
    tableTitle: String,
    textFieldColumnWidth: Dp,
    textFieldHeight: Dp,
    localContext: Context,
    filenamePlaceholder: String,
    buttonFunction: () -> Unit,
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
            modifier = Modifier.width(textFieldColumnWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = filenamePlaceholder,
                onValueChange = {
                    filenameOnEvent(it)
                },
                placeholder = { LittleBodyText(text = "Insert here the ${cardTitle.lowercase()} file name") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.height(textFieldHeight),
                singleLine = true
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.width(0.dp))
                if (icon != null) {
                    if (DataExchangeTypeEnum.IMPORT.type.equals(cardTitle, ignoreCase = true)) {
                        IconShadowButton(
                            onClick = {
                                reloadFunction()
                                Toast.makeText(
                                    localContext,
                                    "Reloaded filename",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Reload"
                        )
                        Spacer(modifier = Modifier.width(0.dp))
                    }
                    IconShadowButton(
                        onClick = {
                            buttonFunction()
                            Toast.makeText(
                                localContext,
                                "Successfully ${cardTitle.lowercase()}ed",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        imageVector = icon!!,
                        contentDescription = "Filename Button"
                    )
                }
            }
        }
    }
}