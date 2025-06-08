package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun DialogFormSectionDescription(
    text: String, descriptionFontSize: TextUnit
) {
    Text(
        text = text,
        fontSize = descriptionFontSize,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary
    )
}