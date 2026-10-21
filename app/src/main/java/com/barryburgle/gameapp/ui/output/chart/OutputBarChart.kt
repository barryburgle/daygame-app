package com.barryburgle.gameapp.ui.output.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.graphics.Canvas
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.utilities.button.LittleIconButton
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun OutputBarChart(
    barEntryList: List<BarEntry>,
    description: String,
    integerValues: Boolean,
    ratio: Boolean,
    categories: List<String>? = null,
    statsLoadInfo: StatsLoadInfoEnum,
    onEvent: (StatsEvent) -> Unit,
    showTitleAndInfo: Boolean = true
) {
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val inChartTextSize = 12f
    Column(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.surface, Shapes.large
        )
    ) {
        if (showTitleAndInfo) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                SmallTitleText(description)
                LittleIconButton(
                    imageVector = Icons.Filled.Info,
                    onClick = { onEvent(StatsEvent.ShowInfo(statsLoadInfo)) }
                )
            }
        }
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
            val barChart = styleBarChart(
                BarChart(context),
                surfaceColor,
                barEntryList,
                ratio,
                categories
            )
            // Rounded bars via a custom renderer.
            val density = context.resources.displayMetrics.density
            barChart.renderer = RoundedBarChartRenderer(
                barChart,
                barChart.animator,
                barChart.viewPortHandler,
                6f * density
            )
            val formatter: ValueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
            val leftAxis: YAxis = barChart.getAxisLeft()
            leftAxis.setValueFormatter(formatter)
            val dataset =
                BarDataSet(barEntryList, description).apply {
                    color = onSurfaceColor
                    valueTextColor = onSurfaceColor
                    valueTextSize = inChartTextSize
                    setDrawValues(true)
                    if (integerValues) {
                        valueFormatter = IntegerValueFormatter()
                    }
                    isHighlightEnabled = true
                    setGradientColor(surfaceColor, onSurfaceColor)
                    styleXAxis(barChart, onSurfaceColor, categories)
                }
            val barData = BarData(dataset)
            barChart.data = barData
            barChart.setFitBars(true)
            // Grow bars from left to right (wavy) and with a soft, springy vertical
            // animation. Each bar gets its own slice of the timeline, so scale the
            // total duration with the bar count and clamp it to keep the sequential
            // bounces slow and gentle without dragging on forever.
            val bounceMillisPerBar = 220
            val animationMillis =
                (barEntryList.size * bounceMillisPerBar).coerceIn(1600, 3200)
            barChart.animateX(animationMillis, Easing.EaseInOutCubic)
            barChart.invalidate()
            barChart
        })
    }
}

fun styleBarChart(
    barChart: BarChart,
    surfaceColor: Int,
    barEntryList: List<BarEntry>,
    ratio: Boolean,
    categories: List<String>? = null
): BarChart {
    barChart.apply {
        setBackgroundColor(surfaceColor)
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = false
            if (!ratio) {
                axisMinimum = -1f
                val max = barEntryList.maxOf { barEntry -> barEntry.y }.toInt()
                axisMaximum = max.toFloat() + 1
            } else {
                axisMinimum = -.05f
                axisMaximum = 1.05f
            }
        }
        xAxis.apply {
            isEnabled = true
            setDrawGridLines(false)
        }
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
        extraBottomOffset = 35f
    }
    return barChart
}

class RoundedBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val cornerRadius: Float
) : BarChartRenderer(chart, animator, viewPortHandler) {

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)

        val drawBorder = dataSet.barBorderWidth > 0f

        val phaseX = mAnimator.phaseX

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, 1f)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)

        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        var j = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break

            if (!isSingleColor) {
                mRenderPaint.color = dataSet.getColor(j / 4)
            }

            if (dataSet.gradientColor != null) {
                val gc = dataSet.gradientColor
                mRenderPaint.shader = android.graphics.LinearGradient(
                    buffer.buffer[j], buffer.buffer[j + 3],
                    buffer.buffer[j], buffer.buffer[j + 1],
                    gc.startColor, gc.endColor,
                    android.graphics.Shader.TileMode.MIRROR
                )
            }

            if (dataSet.gradientColors != null) {
                val gc = dataSet.getGradientColor(j / 4)
                mRenderPaint.shader = android.graphics.LinearGradient(
                    buffer.buffer[j], buffer.buffer[j + 3],
                    buffer.buffer[j], buffer.buffer[j + 1],
                    gc.startColor, gc.endColor,
                    android.graphics.Shader.TileMode.MIRROR
                )
            }

            val barIndex = j / 4
            val entryCount = dataSet.entryCount.coerceAtLeast(1)
            val localProgress =
                ((phaseX * entryCount) - barIndex).coerceIn(0f, 1f)
            val heightScale = springProgress(localProgress)
            val barLeft = buffer.buffer[j]
            val barTop = buffer.buffer[j + 1]
            val barRight = buffer.buffer[j + 2]
            val barBottom = buffer.buffer[j + 3]
            val scaledTop = barBottom - (barBottom - barTop) * heightScale

            c.drawRoundRect(
                barLeft, scaledTop,
                barRight, barBottom,
                cornerRadius, cornerRadius, mRenderPaint
            )

            if (drawBorder) {
                c.drawRoundRect(
                    barLeft, scaledTop,
                    barRight, barBottom,
                    cornerRadius, cornerRadius, mBarBorderPaint
                )
            }
            j += 4
        }
    }

    private fun springProgress(t: Float): Float {
        if (t <= 0f) return 0f
        if (t >= 1f) return 1f
        val p = t - 1f
        // Ease-out back with a gentle overshoot (~4%): the bar eases up just past
        // its final height and settles back down. The default back-out constant
        // (1.70158) overshoots ~10%, which reads as a hard snap.
        val overshoot = 1f
        val c3 = overshoot + 1f
        return 1f + c3 * p * p * p + overshoot * p * p
    }
}

fun styleXAxis(
    barChart: BarChart, textColor: Int, categories: List<String>? = null
) {
    val xAxisFormatter = IntegerValueFormatter()
    xAxisFormatter.label = ""
    val xAxis: XAxis = barChart.getXAxis()
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    if (categories != null) {
        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
    } else {
        xAxis.valueFormatter = xAxisFormatter
    }
    xAxis.textSize = 15f
    xAxis.textColor = textColor
    xAxis.setDrawGridLines(false)
    xAxis.setDrawAxisLine(false)
    xAxis.yOffset = 2f
    xAxis.granularity = 1f
}
