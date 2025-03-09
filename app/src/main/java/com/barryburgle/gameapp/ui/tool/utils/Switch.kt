package com.barryburgle.gameapp.ui.tool.utils

import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable

@Composable
fun Switch(
    flag: Boolean,
    onCheckedChange: () -> Unit
) {
    androidx.compose.material3.Switch(
        checked = flag,
        onCheckedChange = {
            onCheckedChange()
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = ThumbColor(flag),
            checkedTrackColor = TrackColor(flag),
            uncheckedThumbColor = ThumbColor(flag),
            uncheckedTrackColor = TrackColor(flag)
        )
    )
}