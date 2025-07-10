package com.barryburgle.gameapp.ui.tool.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun RowTitle(leftTitle: String, rigthTitle: String, textFieldColumnWidth: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.width(textFieldColumnWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallTitleText(leftTitle)
        }
        Column(
            modifier = Modifier.width(75.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallTitleText(rigthTitle)
        }
    }
}