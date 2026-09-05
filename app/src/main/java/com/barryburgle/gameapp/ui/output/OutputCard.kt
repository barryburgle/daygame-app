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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.barryburgle.gameapp.ui.output.chart.LabeledBarEntry
import com.barryburgle.gameapp.ui.output.chart.OutputLineChart
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import java.util.UUID

private data class FullScreenChartState(
    val labeledEntries: List<LabeledBarEntry>,
    val integerValues: Boolean,
    val movingAverageWindow: Int
)

private var activeFullScreenState by mutableStateOf<FullScreenChartState?>(null)

private var dialogHostId by mutableStateOf<String?>(null)

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
    labeledEntries: List<LabeledBarEntry>,
    integerValues: Boolean,
    movingAverageWindow: Int,
    lastShown: Int
) {
    val cardId = remember { UUID.randomUUID().toString() }

    Card(
        modifier = modifier.clickable {
            activeFullScreenState = FullScreenChartState(
                labeledEntries = labeledEntries,
                integerValues = integerValues,
                movingAverageWindow = movingAverageWindow
            )
        },
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            OutputLineChart(
                labeledEntries = labeledEntries.takeLast(lastShown),
                description = chartLabel,
                integerValues = integerValues,
                movingAverageWindow = movingAverageWindow,
                isScrollable = false
            )
        }
    }

    Spacer(modifier = Modifier.width(5.dp))
    if (activeFullScreenState != null) {
        LaunchedEffect(activeFullScreenState, dialogHostId) {
            if (dialogHostId == null) {
                dialogHostId = cardId
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                if (dialogHostId == cardId) {
                    dialogHostId = null
                }
            }
        }
        if (dialogHostId == cardId) {
            val currentData = activeFullScreenState!!
            val context = LocalContext.current

            fun setOrientation(orientation: Int) {
                var currentContext = context
                var activity: Activity? = null
                while (currentContext is ContextWrapper) {
                    if (currentContext is Activity) {
                        activity = currentContext
                        break
                    }
                    currentContext = currentContext.baseContext
                }
                activity?.requestedOrientation = orientation
            }

            DisposableEffect(Unit) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                onDispose {
                    // Left empty intentionally to prevent loop
                }
            }

            Dialog(
                onDismissRequest = {
                    setOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    activeFullScreenState = null
                    dialogHostId = null
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    OutputLineChart(
                        labeledEntries = currentData.labeledEntries,
                        integerValues = currentData.integerValues,
                        movingAverageWindow = currentData.movingAverageWindow,
                        isScrollable = true,
                        modifier = Modifier.fillMaxSize(),
                        showLabels = true
                    )
                    IconShadowButton(
                        onClick = {
                            setOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            activeFullScreenState = null
                            dialogHostId = null
                        },
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
}