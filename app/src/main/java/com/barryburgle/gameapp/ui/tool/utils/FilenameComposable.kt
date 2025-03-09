package com.barryburgle.gameapp.ui.tool.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FilenameComposable(
    cardTitle: String,
    tableTitle: String,
    textFieldColumnWidth: Dp,
    textFieldHeight: Dp,
    localContext: Context,
    filenamePlaceholder: String,
    buttonFunction: () -> Unit,
    filenameOnEvent: (String) -> Unit
) {
    RowTitle(
        "${cardTitle} ${tableTitle}s file name:",
        "${tableTitle.replaceFirstChar { it.uppercase() }}s:",
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
                modifier = Modifier
                    .height(textFieldHeight)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    buttonFunction()
                    Toast.makeText(
                        localContext,
                        "Successfully ${cardTitle.lowercase()}ed",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                Text(
                    text = cardTitle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}