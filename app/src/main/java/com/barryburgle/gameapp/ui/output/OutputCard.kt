package com.barryburgle.gameapp.ui.output

import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.barryburgle.gameapp.ui.output.chart.OutputLineChart
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.github.mikephil.charting.data.BarEntry

@Composable
fun OutputCard(
    height: Dp,
    width: Dp,
    chartLabel: String,
    modifier: Modifier = Modifier
        .height(height)
        .width(width)
        .shadow(
            elevation = 5.dp,
            shape = MaterialTheme.shapes.large
        ),
    barEntryList: List<BarEntry>,
    integerValues: Boolean,
    movingAverageWindow: Int,
    lastShown: Int
) {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier.clickable { isFullScreen = true },
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            OutputLineChart(
                barEntryList = barEntryList.takeLast(lastShown),
                description = chartLabel,
                integerValues = integerValues,
                movingAverageWindow = movingAverageWindow,
                isScrollable = false
            )
        }
    }
    Spacer(modifier = Modifier.width(5.dp))
    if (isFullScreen) {
        // TODO: we need to support x-axis labels for all charts
        // This means rethinking db queries with query-level labels generation
        // Labels should be then carried on an additional field at this level through barEntries or wrapper object
        val context = LocalContext.current

        DisposableEffect(Unit) {
            var currentContext = context
            var activity: Activity? = null
            while (currentContext is ContextWrapper) {
                if (currentContext is Activity) {
                    activity = currentContext
                    break
                }
                currentContext = currentContext.baseContext
            }
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            onDispose {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }

        Dialog(
            onDismissRequest = { isFullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                OutputLineChart(
                    barEntryList = barEntryList,
                    integerValues = integerValues,
                    movingAverageWindow = movingAverageWindow,
                    isScrollable = true,
                    modifier = Modifier.fillMaxSize()
                )
                IconShadowButton(
                    onClick = { isFullScreen = false },
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Close full screen",
                    boxModifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
    }
}