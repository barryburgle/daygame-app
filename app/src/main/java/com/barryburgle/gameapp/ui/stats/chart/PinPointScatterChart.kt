package com.barryburgle.gameapp.ui.stats.chart

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.PinPoint
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDateTime
import kotlin.random.Random

@Composable
fun OutputScatterChart(
    pinPoints: List<PinPoint>
) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val gridColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f).toArgb()
    val inChartTextSize = 12f
    val themeRed = Color.red(MaterialTheme.colorScheme.primary.toArgb())
    val themeGreen = Color.green(MaterialTheme.colorScheme.primary.toArgb())
    val themeBlue = Color.blue(MaterialTheme.colorScheme.primary.toArgb())

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
                    axisMinimum = 8f
                    axisMaximum = 24f
                    granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val hour = value.toInt()
                            return when {
                                hour == 12 -> "12 PM"
                                hour == 24 || hour == 0 -> "12 AM"
                                hour > 12 -> "${hour - 12} PM"
                                else -> "$hour AM"
                            }
                        }
                    }
                }

                legend.isEnabled = false

                val layer1Entries = mutableListOf<Entry>()
                val layer1Colors = mutableListOf<Int>()
                val layer2Entries = mutableListOf<Entry>()
                val layer2Colors = mutableListOf<Int>()
                val layer3Entries = mutableListOf<Entry>()
                val layer3Colors = mutableListOf<Int>()

                pinPoints.forEach { pinPoint ->
                    val dayBase = pinPoint.dayOfWeek.toFloat()
                    val randomOffset = (Random.nextFloat() - 0.5f) * 0.6f
                    val dayWithOffset = dayBase + randomOffset

                    val timeVal = try {
                        val ldt = LocalDateTime.parse(pinPoint.localTimestamp.substring(0, 19))
                        ldt.hour + ldt.minute / 60f
                    } catch (e: Exception) {
                        12f
                    }

                    if (timeVal >= 8f) {
                        val entry = Entry(dayWithOffset, timeVal)

                        val baseAlpha = when (pinPoint.pinPointType.lowercase()) {
                            PinPointTypeEnum.SET.getField().lowercase() -> 80
                            PinPointTypeEnum.CONVERSATION.getField().lowercase() -> 120
                            PinPointTypeEnum.CONTACT.getField().lowercase() -> 200
                            else -> 80
                        }

                        layer1Entries.add(entry)
                        layer1Colors.add(
                            Color.argb(
                                baseAlpha,
                                themeRed,
                                themeGreen,
                                themeBlue
                            )
                        )

                        layer2Entries.add(entry)
                        layer2Colors.add(
                            Color.argb(
                                (baseAlpha * 0.5f).toInt(),
                                themeRed,
                                themeGreen,
                                themeBlue
                            )
                        )

                        layer3Entries.add(entry)
                        layer3Colors.add(
                            Color.argb(
                                (baseAlpha * 0.15f).toInt(),
                                themeRed,
                                themeGreen,
                                themeBlue
                            )
                        )
                    }
                }

                val set1 = ScatterDataSet(layer1Entries, "Core").apply {
                    colors = layer1Colors
                    setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                    scatterShapeSize = 18f
                    valueTextSize = 0f
                }
                val set2 = ScatterDataSet(layer2Entries, "Mid").apply {
                    colors = layer2Colors
                    setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                    scatterShapeSize = 40f
                    valueTextSize = 0f
                }
                val set3 = ScatterDataSet(layer3Entries, "Outer").apply {
                    colors = layer3Colors
                    setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                    scatterShapeSize = 75f
                    valueTextSize = 0f
                }

                data = ScatterData(set3, set2, set1)
                invalidate()
            }
        }
    )
}