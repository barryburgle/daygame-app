package com.barryburgle.gameapp.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.stats.state.StatsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    state: StatsState
) {
    val spaceFromTop = 20.dp
    val spaceFromBottom = 60.dp
    Scaffold { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = spaceFromTop),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsCard(
                    state = state
                )
            }
            item { Row(modifier = Modifier.height(spaceFromTop + spaceFromBottom)) {} }
        }
    }
}