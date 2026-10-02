package com.barryburgle.gameapp.ui.stats.chart

import android.graphics.Color
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.luminance
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.PinPoint
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.random.Random

private data class ChartPointItem(
    val dayWithOffset: Float,
    val timeVal: Float,
    val baseAlpha: Int
)

@Composable
fun PinPointScatterChart(
    pinPoints: List<PinPoint>
) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val gridColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f).toArgb()
    val inChartTextSize = 12f

    var themeColorArgb = MaterialTheme.colorScheme.primary.toArgb()
    val isThemeColorArgbDark = themeColorArgb.luminance < 0.5f
    if (!isThemeColorArgbDark) {
        themeColorArgb = MaterialTheme.colorScheme.onPrimary.toArgb()
    }
    val themeRed = Color.red(themeColorArgb)
    val themeGreen = Color.green(themeColorArgb)
    val themeBlue = Color.blue(themeColorArgb)

    val chartItems = remember(pinPoints) {
        pinPoints.map { pinPoint ->
            val dayBase = pinPoint.dayOfWeek.toFloat()
            val randomOffset = (Random.nextFloat() - 0.5f) * 0.6f
            val dayWithOffset = dayBase + randomOffset

            val timeVal = try {
                val ldt = LocalDateTime.parse(pinPoint.localTimestamp.substring(0, 19))
                ldt.hour + ldt.minute / 60f
            } catch (e: Exception) {
                12f
            }

            val baseAlpha = when (pinPoint.pinPointType.lowercase()) {
                PinPointTypeEnum.SET.getField().lowercase() -> 80
                PinPointTypeEnum.CONVERSATION.getField().lowercase() -> 120
                PinPointTypeEnum.CONTACT.getField().lowercase() -> 200
                else -> 80
            }

            ChartPointItem(dayWithOffset, timeVal, baseAlpha)
        }.sortedBy { it.dayWithOffset }
    }

    val animatables = remember(chartItems) {
        List(chartItems.size) { Animatable(0f) }
    }

    var animationFrameTick by remember { mutableStateOf(0L) }

    LaunchedEffect(chartItems) {
        if (chartItems.isEmpty()) return@LaunchedEffect

        val renderLoop = launch {
            while (isActive) {
                withFrameNanos { time -> animationFrameTick = time }
            }
        }

        val animJobs = chartItems.indices.map { index ->
            launch {
                delay(index * 10L)
                animatables[index].animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy, // Overshoots diameter (bouncy pop)
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        animJobs.joinAll()
        renderLoop.cancel()
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        factory = { context ->
            ScatterChart(context).apply {
                this.description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    axisMinimum = 0.5f
                    axisMaximum = 7.5f
                    textColor = primaryColor
                    textSize = inChartTextSize
                    setDrawGridLines(false)

                    for (i in 1..6) {
                        val boundary = i + 0.5f
                        addLimitLine(LimitLine(boundary).apply {
                            lineColor = gridColor
                            lineWidth = 0.5f
                            disableDashedLine()
                        })
                    }

                    valueFormatter = object : ValueFormatter() {
                        private val days =
                            arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                        override fun getFormattedValue(value: Float): String {
                            val index = Math.round(value)
                            return if (index in 1..7) days[index] else ""
                        }
                    }
                }

                axisRight.isEnabled = false
                axisLeft.apply {
                    textColor = primaryColor
                    textSize = inChartTextSize
                    granularity = 1f
                    axisMinimum = 11f
                    axisMaximum = 23f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val totalMinutes = (value * 60).toInt()
                            val hour24 = (totalMinutes / 60) % 24
                            val hour12 = when {
                                hour24 == 0 -> 12
                                hour24 > 12 -> hour24 - 12
                                else -> hour24
                            }
                            val amPm = if (hour24 >= 12 && hour24 < 24) "PM" else "AM"
                            return "$hour12 $amPm"
                        }
                    }
                }
                legend.isEnabled = false
            }
        },
        update = { chart ->
            @Suppress("UNUSED_VARIABLE")
            val tick = animationFrameTick

            val dataSets = mutableListOf<IScatterDataSet>()

            chartItems.forEachIndexed { index, item ->
                val scale = animatables.getOrNull(index)?.value ?: 0f

                if (scale > 0.01f) {
                    val entry = Entry(item.dayWithOffset, item.timeVal)

                    val outerColor = Color.argb(
                        (item.baseAlpha * 0.15f).toInt(),
                        themeRed,
                        themeGreen,
                        themeBlue
                    )
                    val midColor = Color.argb(
                        (item.baseAlpha * 0.5f).toInt(),
                        themeRed,
                        themeGreen,
                        themeBlue
                    )
                    val coreColor = Color.argb(item.baseAlpha, themeRed, themeGreen, themeBlue)

                    val setOuter = ScatterDataSet(listOf(entry), "Outer_$index").apply {
                        color = outerColor
                        setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                        scatterShapeSize = (75f * scale).coerceAtLeast(0.1f)
                        setDrawValues(false)
                    }

                    val setMid = ScatterDataSet(listOf(entry), "Mid_$index").apply {
                        color = midColor
                        setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                        scatterShapeSize = (40f * scale).coerceAtLeast(0.1f)
                        setDrawValues(false)
                    }

                    val setCore = ScatterDataSet(listOf(entry), "Core_$index").apply {
                        color = coreColor
                        setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                        scatterShapeSize = (18f * scale).coerceAtLeast(0.1f)
                        setDrawValues(false)
                    }

                    dataSets.add(setOuter)
                    dataSets.add(setMid)
                    dataSets.add(setCore)
                }
            }

            if (dataSets.isNotEmpty()) {
                chart.data = ScatterData(dataSets)
            } else {
                chart.clear()
            }

            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}