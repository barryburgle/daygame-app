package com.barryburgle.gameapp.ui.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

@Composable
fun BlurStatusBar() {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() * 3 / 2
    val semiOpaqueBackground = MaterialTheme.colorScheme.background
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(statusBarHeight)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        semiOpaqueBackground,
                        semiOpaqueBackground.copy(alpha = 1f),
                        androidx.compose.ui.graphics.Color.Transparent
                    ),
                )
            )
    ) {}
}