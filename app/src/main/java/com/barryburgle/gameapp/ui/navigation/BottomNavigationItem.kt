package com.barryburgle.gameapp.ui.navigation

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

data class BottomNavigationItem(
    val title: String,
    val icon: Painter,
    val modifier: Modifier,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val destinationScreen: String,
    var selected: Boolean = false
)