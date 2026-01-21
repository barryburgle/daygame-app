package com.barryburgle.gameapp.ui.input.dialog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun DialogTextComponent(
    value: String,
    placeholder: String,
    height: Dp,
    emptyValue: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { LittleBodyText(placeholder) },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .height(height)
                .fillMaxWidth(0.75f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .height(height),
            verticalArrangement = Arrangement.Center
        ) {
            IconShadowButton(
                onClick = {
                    onValueChange(emptyValue)
                },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete " + placeholder
            )
        }
    }
}