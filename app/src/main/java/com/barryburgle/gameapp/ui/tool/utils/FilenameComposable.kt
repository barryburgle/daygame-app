package com.barryburgle.gameapp.ui.tool.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.DataExchangeTypeEnum

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
                placeholder = { Text(text = "Insert here the ${cardTitle.lowercase()} file name") },
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
                        IconButton(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .size(40.dp),
                            onClick = {
                                reloadFunction()
                                Toast.makeText(
                                    localContext,
                                    "Reloaded filename",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = cardTitle,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .height(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(0.dp))
                    }
                    IconButton(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .size(40.dp),
                        onClick = {
                            buttonFunction()
                            Toast.makeText(
                                localContext,
                                "Successfully ${cardTitle.lowercase()}ed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Icon(
                            imageVector = icon!!,
                            contentDescription = cardTitle,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .height(30.dp)
                        )
                    }
                }
            }
        }
    }
}