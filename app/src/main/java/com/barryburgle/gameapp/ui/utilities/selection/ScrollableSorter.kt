package com.barryburgle.gameapp.ui.utilities.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableSorter(
    spaceFromLeft: Dp, selectionRow: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .width(spaceFromLeft)
        ) {}
        selectionRow()
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .width(spaceFromLeft - 7.dp)
        ) {}
    }
}