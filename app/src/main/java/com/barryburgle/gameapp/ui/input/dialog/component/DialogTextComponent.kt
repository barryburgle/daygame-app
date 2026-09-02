package com.barryburgle.gameapp.ui.input.dialog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.LittleIconButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTextComponent(
    value: String,
    placeholder: String,
    emptyValue: String,
    validContent: Boolean,
    onCopyClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.07f)),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                TextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    placeholder = { LittleBodyText(placeholder) },
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            if (validContent) {
                Column(
                    modifier = Modifier.height(100.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    LittleIconButton(
                        onClick = {
                            onValueChange(emptyValue)
                        },
                        imageVector = Icons.Default.Delete,
                        height = 15.dp
                    )
                    LittleIconButton(
                        onClick = onCopyClick,
                        imageVector = Icons.Default.ContentCopy,
                        height = 15.dp
                    )
                }
            }
        }
    }
}